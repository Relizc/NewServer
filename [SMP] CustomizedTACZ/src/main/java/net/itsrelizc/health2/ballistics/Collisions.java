package net.itsrelizc.health2.ballistics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Collisions {
	
	// Determine hit part based on local vector
    public static int determineHitPart(Vector vec) {
        double y = vec.getY();
        
        ////(y + "");

        // Height zones (approximate for standing player)
        if (y > 1.6) {
            return 0;
        } else if (y > 1) {
            // Upper torso and arms zone
            if (vec.getX() > 0.3) {
                return 3;
            } else if (vec.getX() < -0.3) {
                return 4;
            } else {
                if (y > 1.2) {
                	return 1;
                } else {
                	return 2;
                }
            }
        } else {
        	if (vec.getX() > 0) {
                return 5;
            } else if (vec.getX() < 0) {
                return 6;
            } else {
                return -1;
            }
        }
    }
    
    public enum HitSide {
        LEFT, RIGHT, FRONT, BACK, UNKNOWN
    }
    
    public static class HitDirection {
        public final HitSide cardinal; // FRONT, BACK, LEFT, RIGHT
        public final HitSide leftOrRightOnly; // LEFT or RIGHT only

        public HitDirection(HitSide cardinal, HitSide leftOrRightOnly) {
            this.cardinal = cardinal;
            this.leftOrRightOnly = leftOrRightOnly;
        }
        
        @Override
        public String toString() {
        	return "HitDirection(cardinal=" + this.cardinal + ", strict=" + this.leftOrRightOnly + ")";
        }
    }


    public static HitDirection getHitSide(Location rayOrigin, LivingEntity player) {
        Vector toRay = rayOrigin.toVector().subtract(player.getLocation().toVector());

        // Ignore vertical component
        Vector hitDir = toRay.clone().setY(0).normalize();

        // Get yaw (Spigot yaw: 0 = south, -90 = west, 90 = east, 180 = north)
        float yaw = player.getLocation().getYaw();
        double yawRad = Math.toRadians(yaw);

        // Player's facing direction
        Vector forward = new Vector(-Math.sin(yawRad), 0, Math.cos(yawRad));
        Vector left = new Vector(forward.getZ(), 0, -forward.getX()); // rotate forward 90 deg CCW
        Vector right = left.clone().multiply(-1);
        Vector back = forward.clone().multiply(-1);

        double dotF = hitDir.dot(forward);
        double dotB = hitDir.dot(back);
        double dotL = hitDir.dot(left);
        double dotR = hitDir.dot(right);

        // Determine cardinal hit direction
        HitSide cardinal;
        double max = Math.max(Math.max(dotF, dotB), Math.max(dotL, dotR));
        if (max == dotF) cardinal = HitSide.FRONT;
        else if (max == dotB) cardinal = HitSide.BACK;
        else if (max == dotL) cardinal = HitSide.LEFT;
        else cardinal = HitSide.RIGHT;

        // Determine forced left/right hit direction
        HitSide leftOrRight = (dotL > dotR) ? HitSide.LEFT : HitSide.RIGHT;

        return new HitDirection(cardinal, leftOrRight);
    }
    
    
    private static double dget(Vector v, int dim) {
    	if (dim == 0) return v.getX();
    	if (dim == 1) return v.getY();
    	if (dim == 2) return v.getZ();
    	return 0;
    }
    
    public static enum BodyPart {
        HEAD, CHEST, STOMACH, FEET, NONE, LEGS
    }
    
    public static BodyPart getHitBodyPart(LivingEntity player, Location a, Location b) {
    	
    	////(a + " " + b);
    	
        Location base = player.getLocation();
        Vector origin = a.toVector();
        Vector end = b.toVector();
        Vector dir = end.clone().subtract(origin);
        double totalLength = dir.length();
        Vector normDir = dir.clone().normalize();

        // XZ bounds of player
        double halfWidth = 0.5; // 0.2 bigger than usual
        double minX = base.getX() - halfWidth;
        double maxX = base.getX() + halfWidth;
        double minZ = base.getZ() - halfWidth;
        double maxZ = base.getZ() + halfWidth;

        // Define all hitboxes
        class Hitbox {
            double minY, maxY;
            BodyPart part;
            Hitbox(double minY, double maxY, BodyPart part) {
                this.minY = base.getY() + minY;
                this.maxY = base.getY() + maxY;
                this.part = part;
            }
        }

        Hitbox[] parts = new Hitbox[] {
            new Hitbox(-0.2, 0.4, BodyPart.FEET),
            new Hitbox(0.4, 1.0, BodyPart.LEGS),
            new Hitbox(1.0, 1.1, BodyPart.STOMACH),
            new Hitbox(1.1, 1.6, BodyPart.CHEST),
            new Hitbox(1.6, 2.0, BodyPart.HEAD),
        };

        double closestT = Double.POSITIVE_INFINITY;
        BodyPart closestPart = BodyPart.NONE;

        for (Hitbox hb : parts) {
            Vector boxMin = new Vector(minX, hb.minY, minZ);
            Vector boxMax = new Vector(maxX, hb.maxY, maxZ);

            double tmin = 0;
            double tmax = totalLength;

            for (int i = 0; i < 3; i++) {
                double originComp = dget(origin, i);
                double dirComp = dget(normDir, i);
                double minComp = dget(boxMin, i);
                double maxComp = dget(boxMax, i);

                if (Math.abs(dirComp) < 1e-8) {
                    if (originComp < minComp || originComp > maxComp) {
                        tmin = Double.POSITIVE_INFINITY;
                        break;
                    }
                } else {
                    double ood = 1.0 / dirComp;
                    double t1 = (minComp - originComp) * ood;
                    double t2 = (maxComp - originComp) * ood;
                    if (t1 > t2) {
                        double temp = t1;
                        t1 = t2;
                        t2 = temp;
                    }
                    if (t1 > tmin) tmin = t1;
                    if (t2 < tmax) tmax = t2;
                    if (tmin > tmax) {
                        tmin = Double.POSITIVE_INFINITY;
                        break;
                    }
                }
            }

            if (tmin >= 0 && tmin < closestT && tmin <= totalLength) {
                closestT = tmin;
                closestPart = hb.part;
            }
        }

        return closestPart;
    }
    
    public static boolean isInsideBlock(Location loc) {
        BoundingBox bb = loc.getBlock().getBoundingBox();
        return bb != null && bb.contains(loc.toVector());
    }
    
    public static boolean rayIntersectsPlane(Location rayOrigin, Vector rayDir,
            Vector planeOrigin, Vector planeU, Vector planeV) {

			Vector planeNormal = planeU.clone().crossProduct(planeV);
			double denom = planeNormal.dot(rayDir);
			
			if (Math.abs(denom) < 1e-8) {
			////("parallel");
			return false;
			}
			
			Vector originToPlane = planeOrigin.clone().subtract(rayOrigin.toVector());
			double t = originToPlane.dot(planeNormal) / denom;
			
			if (t < 0) {
			////("behind");
			return false;
			}
			
			Vector intersection = rayOrigin.toVector().add(rayDir.clone().multiply(t));
			Vector local = intersection.clone().subtract(planeOrigin);
			
			double u = local.dot(planeU) / planeU.lengthSquared();
			double v = local.dot(planeV) / planeV.lengthSquared();
			
			////("u=" + u + " v=" + v);
			
			boolean inside = (0 <= u && u <= 1) && (0 <= v && v <= 1);
			//if (!inside) //("not in rect");
			
			return inside;
			}

    
    private static class Face {
    	Vector origin;
    	Vector u;
    	Vector v;
		BlockFace face;
    	
    	public Face(Vector origin, Vector u, Vector v, BlockFace name) {
    		this.origin = origin;
    		this.u = u;
    		this.v = v;
    		this.face = name;
    	}
    	
    	@Override
    	public String toString() {
    		return "Face(face=" + face + ")";
    	}
    }
    
    	static Face west  = new Face(
    	    new Vector(0, 0, 0),           // origin: west face at x=0, lower corner at (0,0,0)
    	    new Vector(0, 1, 0),           // u: vertical edge (up)
    	    new Vector(0, 0, 1),           // v: depth edge (south)
    	    BlockFace.WEST
    	);

    	static Face east  = new Face(
    	    new Vector(1, 0, 0),           // origin: east face at x=1, lower corner at (1,0,0)
    	    new Vector(0, 1, 0),           // u: vertical edge (up)
    	    new Vector(0, 0, 1),           // v: depth edge (south)
    	    BlockFace.EAST
    	);

    	static Face north = new Face(
    	    new Vector(0, 0, 0),           // origin: north face at z=0, lower corner at (0,0,0)
    	    new Vector(1, 0, 0),           // u: horizontal edge (east)
    	    new Vector(0, 1, 0),           // v: vertical edge (up)
    	    BlockFace.NORTH
    	);

    	static Face south = new Face(
    	    new Vector(0, 0, 1),           // origin: south face at z=1, lower corner at (0,0,1)
    	    new Vector(1, 0, 0),           // u: horizontal edge (east)
    	    new Vector(0, 1, 0),           // v: vertical edge (up)
    	    BlockFace.SOUTH
    	);

    	static Face top = new Face(
    	    new Vector(0, 1, 0),           // origin: top face at y=1, lower corner at (0,1,0)
    	    new Vector(0, 0, 1),           // u: horizontal edge (east)
    	    new Vector(1, 0, 0),           // v: depth edge (south)
    	    BlockFace.UP
    	);

    	static Face bottom = new Face(
    	    new Vector(0, 0, 0),           // origin: bottom face at y=0, lower corner at (0,0,0)
    	    new Vector(1, 0, 0),           // u: horizontal edge (east)
    	    new Vector(0, 0, 1),           // v: depth edge (south)
    	    BlockFace.DOWN
    	);
    
    public static BlockFace getHitBlockFace(Location loc, Vector vec, Block b) {
    	
    	List<Face> collidable = new ArrayList<Face>();
    	
    	Location blockpos = b.getLocation();
    	
    	if (loc.getX() < blockpos.getX()) {
    		collidable.add(west);
    	} else if (loc.getX() > blockpos.getX() + 1) {
    		collidable.add(east);
    	}
    	
    	if (loc.getY() < blockpos.getY()) {
    		collidable.add(bottom);
    	} else if (loc.getY() > blockpos.getY() + 1) {
    		collidable.add(top);
    	}
    	
    	if (loc.getZ() < blockpos.getZ()) {
    		collidable.add(north);
    	} else if (loc.getZ() > blockpos.getZ() + 1) {
    		collidable.add(south);
    	}
    	
    	Vector bp = blockpos.toVector();
    	
    	for (Face face : collidable) {
    		Vector bp2 = bp.clone();
    		boolean hits = rayIntersectsPlane(loc, vec, bp2.add(face.origin), face.u.clone(), face.v.clone());
    		
    		if (hits) {
    			return face.face;
    		}
    	}
    	
    	return null;
    }

    private static boolean between(double val, double min, double max) {
        return val >= min && val <= max;
    }

    private static class FaceData {
        Vector normal;
        BlockFace blockFace;
        Vector point;

        FaceData(Vector normal, BlockFace blockFace, Vector point) {
            this.normal = normal;
            this.blockFace = blockFace;
            this.point = point;
        }
    }

}
