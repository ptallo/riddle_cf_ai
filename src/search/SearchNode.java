package search;

import board.Game;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SearchNode {
    private int maxDepth;
    private int currentDepth;
    private Game game;
    private ArrayList<SearchNode> childNodes;

    public SearchNode(Game game, int maxDepth) {
        this.game = game;
        this.maxDepth = maxDepth;
        this.currentDepth = 0;
        childNodes = populateChildren();
    }

    private SearchNode(Game game, int maxDepth, int currentDepth) {
        this.game = game;
        this.maxDepth = maxDepth;
        this.currentDepth = currentDepth;
        childNodes = populateChildren();
    }

    private ArrayList<SearchNode> populateChildren() {
        if (maxDepth > currentDepth) {
            return game.getChildren().stream()
                    .map(node -> new SearchNode(node, maxDepth, currentDepth + 1))
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            return new ArrayList<>();
        }
    }
}
