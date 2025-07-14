package net.itsrelizc.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.World;

import net.citizensnpcs.api.ai.NavigatorParameters;
import net.citizensnpcs.api.ai.PathStrategy;

public class LongDistanceNavigator {
	
	public static class ChunkCoord {
        int x, z;

        ChunkCoord(int x, int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChunkCoord)) return false;
            ChunkCoord other = (ChunkCoord) o;
            return x == other.x && z == other.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, z);
        }

        @Override
        public String toString() {
            return "ChunkCoord{" + "x=" + x + ", z=" + z + '}';
        }
    }
	
	public static class MarkedChunkCoord extends ChunkCoord {
		
	
		private Location selectedLocation;

		MarkedChunkCoord(int x, int z) {
			super(x, z);
			this.selectedLocation = null;
		}
		
		public void setSelectedLocation(Location loc) {
			this.selectedLocation = loc;
		}
		
		@Override
        public String toString() {
            return "ChunkCoord{" + "x=" + x + ", z=" + z + ", selected=" + this.selectedLocation +'}';
        }

		public Location getSelectedLocation() {
			return this.selectedLocation;
		}
		
	}

    private static class Node {
    	MarkedChunkCoord chunk;
        Node parent;
        double g, h;

        Node(MarkedChunkCoord neighbor, Node parent, double g, double h) {
            this.chunk = neighbor;
            this.parent = parent;
            this.g = g;
            this.h = h;
        }

        double f() {
            return g + h;
        }
    }

	private final Set<MarkedChunkCoord> blocked = new HashSet<>();
	private Location startLoc;
	private Location endLoc;
	private World activeWorld;
	private RelizcNPC npc;
	private int current = 0;
	
	public LongDistanceNavigator(RelizcNPC npc, Location start, Location end) {
		this.startLoc = start;
		this.npc = npc;
		this.endLoc = end;
		this.activeWorld = start.getWorld();

	}
	
	private List<MarkedChunkCoord> findChunkPath() {
		MarkedChunkCoord startChunk = new MarkedChunkCoord(startLoc.getChunk().getX(), startLoc.getChunk().getZ());
		MarkedChunkCoord endChunk = new MarkedChunkCoord(endLoc.getChunk().getX(), endLoc.getChunk().getZ());

        // Simple 2D A* on chunk grid here:

        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f()));
        Map<ChunkCoord, Node> allNodes = new HashMap<>();
        Set<MarkedChunkCoord> closedSet = new HashSet<>();

        Node startNode = new Node(startChunk, null, 0, heuristic(startChunk, endChunk));
        openSet.add(startNode);
        allNodes.put(startChunk, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.chunk.equals(endChunk)) {
                return reconstructPath(current);
            }
            closedSet.add(current.chunk);

            for (MarkedChunkCoord neighbor : getChunkNeighbors(current.chunk)) {
                if (blocked.contains(neighbor) || closedSet.contains(neighbor)) continue;

                double tentativeG = current.g + 1;

                Node neighborNode = allNodes.get(neighbor);
                if (neighborNode == null) {
                    neighborNode = new Node(neighbor, current, tentativeG, heuristic(neighbor, endChunk));
                    allNodes.put(neighbor, neighborNode);
                    openSet.add(neighborNode);
                } else if (tentativeG < neighborNode.g) {
                    neighborNode.parent = current;
                    neighborNode.g = tentativeG;
                    // reinsert for priority update:
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // no path found
    }
	
	List<MarkedChunkCoord> coordinates;
	
	public void generalizeChunks() {
		coordinates = this.findChunkPath();
		
		for (MarkedChunkCoord coord : coordinates) {
			Location loc = this.getRandomLocationInChunk(coord);
			coord.setSelectedLocation(loc);
		}
	}
	
	public List<MarkedChunkCoord> getGeneralizedChunkCoordinates() {
		return coordinates;
	}
	
	private List<MarkedChunkCoord> reconstructPath(Node node) {
        LinkedList<MarkedChunkCoord> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node.chunk);
            node = node.parent;
        }
        return path;
    }

    private double heuristic(ChunkCoord a, ChunkCoord b) {
        // Manhattan distance for grid:
        return Math.abs(a.x - b.x) + Math.abs(a.z - b.z);
    }

    private List<MarkedChunkCoord> getChunkNeighbors(ChunkCoord chunk) {
        List<MarkedChunkCoord> neighbors = new ArrayList<>();
        neighbors.add(new MarkedChunkCoord(chunk.x + 1, chunk.z));
        neighbors.add(new MarkedChunkCoord(chunk.x - 1, chunk.z));
        neighbors.add(new MarkedChunkCoord(chunk.x, chunk.z + 1));
        neighbors.add(new MarkedChunkCoord(chunk.x, chunk.z - 1));
        return neighbors;
    }
    
    private Location getRandomLocationInChunk(ChunkCoord chunkCoord) {
    	Random random = new Random();
        int baseX = chunkCoord.x << 4;
        int baseZ = chunkCoord.z << 4;
        int randX = baseX + random.nextInt(16);
        int randZ = baseZ + random.nextInt(16);
        int y = activeWorld.getHighestBlockYAt(randX, randZ);
        return new Location(activeWorld, randX + 0.5, y + 1, randZ + 0.5);
    }

	public Location getNext() {
		Location l = this.coordinates.get(current++).getSelectedLocation();
		System.out.println("NPC querying next location: " + l);
		return l;
		
	}
}
