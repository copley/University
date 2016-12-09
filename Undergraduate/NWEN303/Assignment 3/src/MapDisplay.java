import javax.swing.*;
import java.awt.*;

/**
 * Created by danielbraithwt on 8/23/16.
 */
public class MapDisplay {
    JPanel panel;
    Maze maze;

    public MapDisplay(final Maze maze) {
        this.maze = maze;

        JFrame frame = new JFrame();
        panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                for (int i = 0; i < maze.maze.length; i++) {
                    for (int j = 0; j < maze.maze.length; j++) {
                        g.setColor(Color.white);

                        if (maze.maze[i][j].getType() == MazeCell.Type.WALL) {
                            g.setColor(Color.BLACK);
                        }

                        if (maze.maze[i][j].containsMarking(MazeCell.Marking.EAST)) {
                            g.setColor(Color.lightGray);
                        }

                        if (maze.maze[i][j].containsMarking(MazeCell.Marking.WEST)) {
                            g.setColor(Color.MAGENTA);
                        }

                        if (maze.maze[i][j].containsMarking(MazeCell.Marking.SOUTH)) {
                            g.setColor(Color.BLUE);
                        }

                        if (maze.maze[i][j].containsMarking(MazeCell.Marking.NORTH)) {
                            g.setColor(Color.CYAN);
                        }

                        if (maze.maze[i][j].containsMarking(MazeCell.Marking.GOLD)) {
                            g.setColor(Color.YELLOW);
                        }

                        if (maze.maze[i][j].containsMarking(MazeCell.Marking.DEAD)) {
                            g.setColor(Color.RED);
                        }

                        if (maze.maze[i][j].getVisiting().size() > 0) {
                            g.setColor(Color.GREEN);
                        }

                        g.fillRect(20 * j, 20 * i, 20, 20);
                    }
                }
            }
        };
        frame.add(panel);

        frame.setMinimumSize(new Dimension(500, 500));
        frame.setVisible(true);

    }

    public void refresh() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}

            panel.revalidate();
            panel.repaint();
        }
    }
}
