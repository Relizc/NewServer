
public class RayPlaneIntersection {

    public static class Vector3D {
        public double x, y, z;

        public Vector3D(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }

        public Vector3D subtract(Vector3D other) {
            return new Vector3D(x - other.x, y - other.y, z - other.z);
        }

        public Vector3D add(Vector3D other) {
            return new Vector3D(x + other.x, y + other.y, z + other.z);
        }

        public Vector3D scale(double scalar) {
            return new Vector3D(x * scalar, y * scalar, z * scalar);
        }

        public double dot(Vector3D other) {
            return x * other.x + y * other.y + z * other.z;
        }

        public Vector3D cross(Vector3D other) {
            return new Vector3D(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
            );
        }

        public double lengthSquared() {
            return x * x + y * y + z * z;
        }

        public Vector3D normalize() {
            double len = Math.sqrt(lengthSquared());
            return new Vector3D(x / len, y / len, z / len);
        }
    }

    /**
     * @return true if the ray intersects the finite plane area
     */
    public static boolean rayIntersectsPlane(Vector3D rayOrigin, Vector3D rayDirection,
                                             Vector3D planeOrigin, Vector3D planeU, Vector3D planeV) {
        Vector3D planeNormal = planeU.cross(planeV);
        double denom = planeNormal.dot(rayDirection);

        // If denom is close to 0, ray is parallel to the plane
        if (Math.abs(denom) < 1e-8) {
            return false;
        }

        Vector3D originToPlane = planeOrigin.subtract(rayOrigin);
        double t = originToPlane.dot(planeNormal) / denom;

        // If t < 0, the intersection is behind the ray origin
        if (t < 0) return false;

        // Compute intersection point
        Vector3D intersection = rayOrigin.add(rayDirection.scale(t));

        // Check if the intersection lies within the finite bounds of the plane
        Vector3D local = intersection.subtract(planeOrigin);

        double uLenSq = planeU.lengthSquared();
        double vLenSq = planeV.lengthSquared();
        double uDot = local.dot(planeU);
        double vDot = local.dot(planeV);

        // Projection must fall within [0, |planeU|^2] and [0, |planeV|^2]
        return (0 <= uDot && uDot <= uLenSq) && (0 <= vDot && vDot <= vLenSq);
    }

    // Example usage
    public static void main(String[] args) {
        Vector3D rayOrigin = new Vector3D(0, 0, 0);
        Vector3D rayDir = new Vector3D(1, 1, 1).normalize();

        Vector3D planeOrigin = new Vector3D(5, 5, 5);
        Vector3D planeU = new Vector3D(1, 0, 0);
        Vector3D planeV = new Vector3D(0, 1, 0);

        boolean hit = rayIntersectsPlane(rayOrigin, rayDir, planeOrigin, planeU, planeV);
        System.out.println("Intersects: " + hit);
    }
}

