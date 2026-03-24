#!/usr/bin/env python3
"""
generate_helmet.py — Create a 3D helmet STL file using parametric geometry.

Dependencies:
    pip install numpy numpy-stl

Usage:
    python generate_helmet.py
    python generate_helmet.py --output my_helmet.stl --radius 110 --wall 4 --resolution 80

Helmet anatomy:
    • Spherical dome shell (outer + inner surfaces, 5 mm thick by default)
    • Face opening at the front (forehead-to-chin slot)
    • Flat brim around the entire bottom edge

Coordinate system: +Z = up (top of head), +X = front of helmet.
All dimensions are in millimetres.
"""

import argparse
import numpy as np
from stl import mesh


# ─────────────────────────────────────────────────────────────────────────────
# Coordinate helpers
# ─────────────────────────────────────────────────────────────────────────────

def sph(r, theta, phi):
    """Spherical (r, polar θ, azimuth φ) → Cartesian (x, y, z)."""
    return np.array([
        r * np.sin(theta) * np.cos(phi),
        r * np.sin(theta) * np.sin(phi),
        r * np.cos(theta),
    ])


def cyl(r, phi, z):
    """Cylindrical (r, φ, z) → Cartesian."""
    return np.array([r * np.cos(phi), r * np.sin(phi), z])


# ─────────────────────────────────────────────────────────────────────────────
# Triangle-mesh accumulator
# ─────────────────────────────────────────────────────────────────────────────

class TriMesh:
    """Collects triangles and writes them as a binary STL."""

    def __init__(self):
        self._tris = []

    def tri(self, a, b, c):
        """Add one CCW triangle (outward normal by right-hand rule)."""
        self._tris.append((
            np.asarray(a, dtype=float),
            np.asarray(b, dtype=float),
            np.asarray(c, dtype=float),
        ))

    def quad(self, a, b, c, d):
        """Split a planar quad ABCD into two CCW triangles."""
        self.tri(a, b, c)
        self.tri(a, c, d)

    def save(self, filename):
        n = len(self._tris)
        m = mesh.Mesh(np.zeros(n, dtype=mesh.Mesh.dtype))
        for k, (a, b, c) in enumerate(self._tris):
            m.vectors[k] = [a, b, c]
        m.save(filename)
        return n


# ─────────────────────────────────────────────────────────────────────────────
# Face-opening predicate
# ─────────────────────────────────────────────────────────────────────────────

def in_face_opening(theta, phi, theta_start, phi_half):
    """
    Return True when (θ, φ) falls inside the helmet's face opening.

    The opening is a frontal slot (φ near 0) that starts below the
    forehead line (θ > theta_start) and spans ±phi_half in azimuth.
    """
    phi_n = (phi + np.pi) % (2 * np.pi) - np.pi   # normalise → (−π, π]
    return theta > theta_start and abs(phi_n) < phi_half


# ─────────────────────────────────────────────────────────────────────────────
# Helmet builder
# ─────────────────────────────────────────────────────────────────────────────

def build_helmet(
    outer_r   = 100.0,   # outer dome radius (mm)
    wall      = 5.0,     # shell thickness (mm)
    lat       = 60,      # number of latitude bands
    lon       = 90,      # number of longitude segments
    dome_deg  = 115.0,   # dome extent from top pole (>90 ° reaches below equator)
    face_deg  = 35.0,    # polar angle where face opening starts (forehead line)
    fopen_deg = 70.0,    # half-width of face opening in azimuth
    brim_w    = 12.0,    # brim overhang beyond dome edge (mm)
    brim_h    = 8.0,     # brim vertical thickness (mm)
):
    inner_r  = outer_r - wall
    dome_rad = np.radians(dome_deg)
    ftheta   = np.radians(face_deg)
    fphi     = np.radians(fopen_deg)

    tm = TriMesh()

    thetas = np.linspace(0.0, dome_rad, lat + 1)
    phis   = np.linspace(-np.pi, np.pi, lon + 1)   # phis[0] == phis[-1] == −π

    # Pre-compute all vertex positions: shape [lat+1, lon+1, 3]
    O = np.array([[sph(outer_r, t, p) for p in phis] for t in thetas])
    I = np.array([[sph(inner_r, t, p) for p in phis] for t in thetas])

    def skip_quad(i, j):
        """True when the (i,j) quad centre lies inside the face opening."""
        tc = (thetas[i] + thetas[i + 1]) * 0.5
        pc = (phis[j]   + phis[j + 1])   * 0.5
        return in_face_opening(tc, pc, ftheta, fphi)

    # ── Outer dome surface ───────────────────────────────────────────────────
    for i in range(lat):
        for j in range(lon):
            if skip_quad(i, j):
                continue
            if i == 0:                              # degenerate pole row → triangles
                tm.tri(O[0, 0], O[1, j + 1], O[1, j])
            else:
                tm.quad(O[i, j], O[i+1, j], O[i+1, j+1], O[i, j+1])

    # ── Inner dome surface (normals flipped inward) ──────────────────────────
    for i in range(lat):
        for j in range(lon):
            if skip_quad(i, j):
                continue
            if i == 0:
                tm.tri(I[0, 0], I[1, j], I[1, j + 1])
            else:
                tm.quad(I[i, j], I[i, j+1], I[i+1, j+1], I[i+1, j])

    # ── Bottom annular rim: seals outer & inner shells at dome base ──────────
    for j in range(lon):
        pc = (phis[j] + phis[j + 1]) * 0.5
        if in_face_opening(dome_rad, pc, ftheta, fphi):
            continue
        tm.quad(O[-1, j], I[-1, j], I[-1, j+1], O[-1, j+1])

    # ── Face-opening side walls (vertical strip at each phi boundary) ─────────
    for i in range(1, lat + 1):
        if thetas[i - 1] < ftheta:
            continue                         # row is entirely above the opening
        for j in range(lon):
            here = not in_face_opening(thetas[i], phis[j],   ftheta, fphi)
            nxt  = not in_face_opening(thetas[i], phis[j+1], ftheta, fphi)

            if here and not nxt:             # right boundary of the opening
                tm.quad(O[i, j+1], O[i-1, j+1], I[i-1, j+1], I[i, j+1])
            elif not here and nxt:           # left boundary of the opening
                tm.quad(O[i, j],   I[i, j],   I[i-1, j],   O[i-1, j])

    # ── Face-opening top cap (horizontal band at the forehead line) ──────────
    idx0 = min(int(np.searchsorted(thetas, ftheta, side="left")), lat)
    for j in range(lon):
        pc = (phis[j] + phis[j + 1]) * 0.5
        if not in_face_opening(thetas[idx0], pc, ftheta, fphi):
            continue
        tm.quad(I[idx0, j], I[idx0, j+1], O[idx0, j+1], O[idx0, j])

    # ── Brim: flat overhang encircling the dome base ─────────────────────────
    base_z  = outer_r * np.cos(dome_rad)   # z of the dome's bottom edge
    base_ri = outer_r * np.sin(dome_rad)   # inner brim radius (= dome outer edge)
    base_ro = base_ri + brim_w             # outer brim radius
    base_zt = base_z                       # brim top surface z
    base_zb = base_z - brim_h             # brim bottom surface z

    for j in range(lon):
        a0, a1 = phis[j], phis[j + 1]

        Ti  = cyl(base_ri, a0, base_zt);  To  = cyl(base_ro, a0, base_zt)
        Ti1 = cyl(base_ri, a1, base_zt);  To1 = cyl(base_ro, a1, base_zt)
        Bi  = cyl(base_ri, a0, base_zb);  Bo  = cyl(base_ro, a0, base_zb)
        Bi1 = cyl(base_ri, a1, base_zb);  Bo1 = cyl(base_ro, a1, base_zb)

        tm.quad(Ti,  To,  To1, Ti1)    # top face    (normal +Z)
        tm.quad(Bi1, Bo1, Bo,  Bi)     # bottom face (normal −Z)
        tm.quad(To,  Bo,  Bo1, To1)    # outer wall  (normal +R)
        tm.quad(Ti1, Bi1, Bi,  Ti)     # inner wall  (normal −R)

    return tm


# ─────────────────────────────────────────────────────────────────────────────
# CLI entry point
# ─────────────────────────────────────────────────────────────────────────────

def main():
    ap = argparse.ArgumentParser(
        description="Generate a 3D helmet STL file.",
        formatter_class=argparse.ArgumentDefaultsHelpFormatter,
    )
    ap.add_argument("--output",     default="helmet.stl",
                    help="Output STL filename")
    ap.add_argument("--radius",     type=float, default=100.0,
                    help="Outer dome radius in mm (scale to head size)")
    ap.add_argument("--wall",       type=float, default=5.0,
                    help="Shell wall thickness in mm")
    ap.add_argument("--resolution", type=int,   default=60,
                    help="Mesh resolution: number of latitude bands (higher = smoother)")
    args = ap.parse_args()

    print("Building helmet mesh …")
    tm = build_helmet(
        outer_r = args.radius,
        wall    = args.wall,
        lat     = args.resolution,
        lon     = args.resolution * 3 // 2,
    )
    n = tm.save(args.output)
    print(f"Saved '{args.output}' — {n:,} triangles.")
    print("Open the file in MeshLab, PrusaSlicer, or any STL viewer to inspect it.")


if __name__ == "__main__":
    main()
