public class Minesweeper {





    /**
     * Bootleg Microsoft Minesweeper game
     */
    public static void minesweeperGame() {
        System.out.println("E - Easy (9 х 9, 10 mines)");
        System.out.println("M - Medium (16 х 16, 40 mines)");
        System.out.println("H - Hard (16 x 30, 99 mines");
        System.out.println("any other input - Custom");
        System.out.print("Select difficulty: ");
        int columns, lines, mines;
        boolean gameOver = false;
        Scanner sc = new Scanner(System.in);
        char letter = sc.next().charAt(0);
        if (letter == 'e' || letter == 'E') {
            columns = 9;
            lines = 9;
            mines = 10;
        } else if (letter == 'm' || letter == 'M') {
            columns = 16;
            lines = 16;
            mines = 40;
        } else if (letter == 'h' || letter == 'H') {
            columns = 30;
            lines = 16;
            mines = 99;
        } else {
            System.out.print("Input number of columns: ");
            columns = sc.nextInt();
            do {
                System.out.print("Input number of lines (26 or less): ");
                lines = sc.nextInt();
                if (lines > 26) {
                    System.out.println("Too many lines!");
                }
            } while (lines > 26);
            do {
                System.out.print("Input number of mines: ");
                mines = sc.nextInt();
                if (mines >= lines * columns) {
                    System.out.println("Too many mines!");
                }
            } while (mines > lines * columns);
        }
        char[][] gameField = new char[lines][columns];
        char[][] gameField2;
        for (int m = 0; m < gameField.length; m++) {
            for (int n = 0; n < gameField[0].length; n++) {
                gameField[m][n] = ' ';
            }
        }
        Minesweeper.showMinefield(gameField);
        int selectedLine = getSelectedLine(lines, sc);
        int selectedColumn = getSelectedColumn(columns, sc);
        do {
            gameField2 = Minesweeper.createMinefield(columns, lines, mines);
        } while (gameField2[selectedLine][selectedColumn] == '*');
        Minesweeper.openCell(gameField, gameField2, selectedLine, selectedColumn);
        Minesweeper.showMinefield(gameField);
        int selectedAction;
        int mineCounter;
        while (!gameOver) {
            mineCounter = 0;
            for (char[] chars : gameField) {
                for (int n = 0; n < gameField[0].length; n++) {
                    if (chars[n] == '⚑') {
                        mineCounter++;
                    }
                }
            }
            System.out.println("There are " + (mines - mineCounter) + "/" + mines + " un-flagged mines remaining!");
            mineCounter = 0;
            selectedLine = getSelectedLine(lines, sc);
            selectedColumn = getSelectedColumn(columns, sc);
            System.out.println("Select Action:");
            System.out.println("(1) - open cell");
            System.out.println("(2) - place/remove a flag");
            System.out.println("(3) - open cell and all un-flagged cells adjacent to it");
            System.out.println("Any other input - change selected cell");
            selectedAction = sc.nextInt();
            if (selectedAction == 1) {
                Minesweeper.openCell(gameField, gameField2, selectedLine, selectedColumn);
                Minesweeper.showMinefield(gameField);
            } else if (selectedAction == 2) {
                Minesweeper.placeFlag(gameField, selectedLine, selectedColumn);
                Minesweeper.showMinefield(gameField);
            } else if (selectedAction == 3) {
                Minesweeper.openAdjacentCells(gameField, gameField2, selectedLine, selectedColumn);
                Minesweeper.showMinefield(gameField);
            }
            for (char[] chars : gameField) {
                for (int n = 0; n < gameField[0].length; n++) {
                    if (chars[n] == '*') {
                        gameOver = true;
                        System.out.println("You have lost the game!");
                        System.out.println("Game over!");
                        Minesweeper.showMinefield(gameField2);
                    }
                }
            }
            if (!(gameOver)) {
                for (int m = 0; m < gameField.length; m++) {
                    for (int n = 0; n < gameField[0].length; n++) {
                        if ((gameField2[m][n] == '*') || (gameField2[m][n] == gameField[m][n])) {
                            mineCounter++;
                        }
                    }
                }
                if (mineCounter == lines * columns) {
                    gameOver = true;
                    System.out.println("You have won the game!");
                    System.out.println("Game over!");
                    Minesweeper.showMinefield(gameField2);
                }
            }
        }
    }

    private static int getSelectedLine(int lines, Scanner sc) {
        int selectedLine;
        do {
            System.out.print("Select line using letters (for example, A):  ");
            selectedLine = sc.next().charAt(0);
            if (!(((selectedLine > 64) && (selectedLine <= 64 + lines)) || ((selectedLine > 96) && (selectedLine <= 96 + lines)))) {
                System.out.println("Incorrect line selected!");
            }
        } while (!(((selectedLine > 64) && (selectedLine <= 64 + lines)) || ((selectedLine > 96) && (selectedLine <= 96 + lines))));
        if (selectedLine < 91) {
            selectedLine -= 65;
        } else {
            selectedLine -= 97;
        }
        return selectedLine;
    }

    private static int getSelectedColumn(int columns, Scanner sc) {
        int selectedColumn;
        do {
            System.out.print("Select column (for example, 1):  ");
            selectedColumn = sc.nextInt();
            if (!((selectedColumn > 0) && (selectedColumn <= columns))) {
                System.out.println("Incorrect column selected!");
            }
        } while (!((selectedColumn > 0) && (selectedColumn <= columns)));
        selectedColumn--;
        return selectedColumn;
    }

    /**
     * Opens cell selected by player. If cell returns 0, opens all adjacent cells.
     *
     * @param gameField      minefield as seen by player
     * @param gameField2     minefield with all cells open
     * @param selectedLine   line number selected by player
     * @param selectedColumn column number selected by player
     */
    public static void openCell(char[][] gameField, char[][] gameField2, int selectedLine, int selectedColumn) {
        if ((!(gameField[selectedLine][selectedColumn] == '⚑')) && (gameField2[selectedLine][selectedColumn] == '0') && (!(gameField[selectedLine][selectedColumn] == gameField2[selectedLine][selectedColumn]))) {
            Minesweeper.openAdjacentCells(gameField, gameField2, selectedLine, selectedColumn);
        } else {
            if ((!((gameField[selectedLine][selectedColumn] == '⚑'))) && !(gameField[selectedLine][selectedColumn] == gameField2[selectedLine][selectedColumn])) {
                gameField[selectedLine][selectedColumn] = gameField2[selectedLine][selectedColumn];
            }
        }
    }

    /**
     * Opens cell selected by player and all un-flagged cells adjacent to it. If cell returns 0, opens all adjacent cells.
     *
     * @param gameField      minefield as seen by player
     * @param gameField2     minefield with all cells open
     * @param selectedLine   line number selected by player
     * @param selectedColumn column number selected by player
     */
    public static void openAdjacentCells(char[][] gameField, char[][] gameField2, int selectedLine, int selectedColumn) {
        if ((!((gameField[selectedLine][selectedColumn] == '⚑'))) && !(gameField[selectedLine][selectedColumn] == gameField2[selectedLine][selectedColumn])) {
            gameField[selectedLine][selectedColumn] = gameField2[selectedLine][selectedColumn];
        }
        if ((selectedLine + 1) < gameField.length && (selectedColumn + 1) < gameField[0].length) {
            if ((!((gameField[selectedLine + 1][selectedColumn + 1] == '⚑'))) && !(gameField[selectedLine + 1][selectedColumn + 1] == gameField2[selectedLine + 1][selectedColumn + 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine + 1, selectedColumn + 1);
            }
            if ((!((gameField[selectedLine + 1][selectedColumn] == '⚑'))) && !(gameField[selectedLine + 1][selectedColumn] == gameField2[selectedLine + 1][selectedColumn])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine + 1, selectedColumn);
            }
            if ((!(gameField[selectedLine][selectedColumn + 1] == '⚑')) && !(gameField[selectedLine][selectedColumn + 1] == gameField2[selectedLine][selectedColumn + 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine, selectedColumn + 1);
            }
        }
        if ((selectedLine + 1) < gameField.length && (selectedColumn - 1) >= 0) {
            if ((!(gameField[selectedLine + 1][selectedColumn - 1] == '⚑')) && !(gameField[selectedLine + 1][selectedColumn - 1] == gameField2[selectedLine + 1][selectedColumn - 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine + 1, selectedColumn - 1);
            }
            if ((!((gameField[selectedLine + 1][selectedColumn] == '⚑'))) && !(gameField[selectedLine + 1][selectedColumn] == gameField2[selectedLine + 1][selectedColumn])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine + 1, selectedColumn);
            }
            if ((!(gameField[selectedLine][selectedColumn - 1] == '⚑')) && !(gameField[selectedLine][selectedColumn - 1] == gameField2[selectedLine][selectedColumn - 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine, selectedColumn - 1);
            }
        }
        if ((selectedLine - 1) >= 0 && (selectedColumn + 1) < gameField[0].length) {
            if ((!(gameField[selectedLine - 1][selectedColumn + 1] == '⚑')) && !(gameField[selectedLine - 1][selectedColumn + 1] == gameField2[selectedLine - 1][selectedColumn + 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine - 1, selectedColumn + 1);
            }
            if ((!(gameField[selectedLine][selectedColumn + 1] == '⚑')) && !(gameField[selectedLine][selectedColumn + 1] == gameField2[selectedLine][selectedColumn + 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine, selectedColumn + 1);
            }
            if ((!(gameField[selectedLine - 1][selectedColumn] == '⚑')) && !(gameField[selectedLine - 1][selectedColumn] == gameField2[selectedLine - 1][selectedColumn])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine - 1, selectedColumn);
            }
        }
        if ((selectedLine - 1) >= 0 && (selectedColumn - 1) >= 0) {
            if ((!(gameField[selectedLine - 1][selectedColumn - 1] == '⚑')) && !(gameField[selectedLine - 1][selectedColumn - 1] == gameField2[selectedLine - 1][selectedColumn - 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine - 1, selectedColumn - 1);
            }
            if ((!(gameField[selectedLine][selectedColumn - 1] == '⚑')) && !(gameField[selectedLine][selectedColumn - 1] == gameField2[selectedLine][selectedColumn - 1])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine, selectedColumn - 1);
            }
            if ((!(gameField[selectedLine - 1][selectedColumn] == '⚑')) && !(gameField[selectedLine - 1][selectedColumn] == gameField2[selectedLine - 1][selectedColumn])) {
                Minesweeper.openCell(gameField, gameField2, selectedLine - 1, selectedColumn);
            }
        }
    }

    /**
     * Places a flag on empty cell selected by player. If cell returns flag, removes it.
     *
     * @param gameField      minefield as seen by player
     * @param selectedLine   line number selected by player
     * @param selectedColumn column number selected by player
     */
    public static void placeFlag(char[][] gameField, int selectedLine, int selectedColumn) {
        if (gameField[selectedLine][selectedColumn] == ' ') {
            gameField[selectedLine][selectedColumn] = '⚑';
        } else if (gameField[selectedLine][selectedColumn] == '⚑') {
            gameField[selectedLine][selectedColumn] = ' ';
        }
    }

    /**
     * Populates the field randomly with mines (*) and correct mine count values (from 0 to 8).
     *
     * @param columns - pre-set width of the minefield
     * @param lines   - pre-set height of the minefield
     * @param mines   - number of mines on the minefield
     * @return fully populated minefield
     */
    public static char[][] createMinefield(int columns, int lines, int mines) {
        char[][] minefield = new char[lines][columns];
        int[] randomCoordinates = new int[2];
        char[] mineCountCheck;
        char mineCounter;
        while (mines > 0) {
            randomCoordinates[0] = (int) (Math.random() * lines);
            randomCoordinates[1] = (int) (Math.random() * columns);
            if (!(minefield[randomCoordinates[0]][randomCoordinates[1]] == '*')) {
                minefield[randomCoordinates[0]][randomCoordinates[1]] = '*';
                mines--;
            }
            for (int m = 0; m < minefield.length; m++) {
                for (int n = 0; n < minefield[0].length; n++) {
                    mineCountCheck = new char[]{'0', '0', '0', '0', '0', '0', '0', '0'};
                    mineCounter = 0;
                    if (!(minefield[m][n] == '*')) {
                        if ((m + 1) < minefield.length && (n + 1) < minefield[0].length) {
                            mineCountCheck[1] = minefield[m + 1][n + 1];
                            mineCountCheck[5] = minefield[m + 1][n];
                            mineCountCheck[6] = minefield[m][n + 1];
                        }
                        if ((m + 1) < minefield.length && (n - 1) >= 0) {
                            mineCountCheck[2] = minefield[m + 1][n - 1];
                            mineCountCheck[5] = minefield[m + 1][n];
                            mineCountCheck[7] = minefield[m][n - 1];
                        }
                        if ((m - 1) >= 0 && (n + 1) < minefield[0].length) {
                            mineCountCheck[3] = minefield[m - 1][n + 1];
                            mineCountCheck[6] = minefield[m][n + 1];
                            mineCountCheck[0] = minefield[m - 1][n];
                        }
                        if ((m - 1) >= 0 && (n - 1) >= 0) {
                            mineCountCheck[4] = minefield[m - 1][n - 1];
                            mineCountCheck[7] = minefield[m][n - 1];
                            mineCountCheck[0] = minefield[m - 1][n];
                        }
                        for (char a : mineCountCheck) {
                            if (a == '*') {
                                mineCounter++;
                            }
                        }
                        minefield[m][n] = '0';
                        minefield[m][n] += mineCounter;
                    }
                }
            }

        }
        return minefield;
    }

    /**
     * prints minefield array as a spreadsheet with grid coordinates
     *
     * @param minefield - minefield
     */
    public static void showMinefield(char[][] minefield) {
        char lineCounter = 'A';
        System.out.print("  ");
        for (int n = 0; n < minefield[0].length; n++) {
            if (n + 1 < 10) {
                System.out.print("  " + (n + 1));
            } else System.out.print(" " + (n + 1));

        }
        System.out.println();
        System.out.print("  ");
        for (int n = 0; n < minefield[0].length; n++) {
            System.out.print("===");
        }
        System.out.print("=");
        System.out.println();
        for (char[] chars : minefield) {
            System.out.print((lineCounter) + " ║");
            lineCounter++;
            for (int n = 0; n < minefield[0].length; n++) {
                System.out.print("[" + chars[n] + "]");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Minesweeper.minesweeperGame();
    }
}
