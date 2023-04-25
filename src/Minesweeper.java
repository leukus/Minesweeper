
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Minesweeper extends Application {
	protected static MinesweeperButton buttons[][] = new MinesweeperButton[8][8];
	protected static int height = 8;
	protected static int width = 8;
	protected static int numGuessed = 0;
	protected static boolean gameOver = false;
	protected static boolean reset = false;
	protected static int firstClick = 1;
	protected static int size = 8;
	protected static int grid[][] = new int[height][width];
	protected static int heatmap[][] = new int[height][width];
	protected static int resetZero[][] = new int[height][width];
	protected static int numTiles = size * size;
	protected static int numBombs = 10;
	protected static int alteredNumBombs = 10;
	protected static int tilesCleared = 0;
	protected static int numSafe = numTiles - numBombs;
	protected static int secondsElapsed = 0;
	protected static int resetCount = 0;
	protected static int timerOne, timerTwo, timerThree;

	public static void main(String[] args) {
		launch(args);
		System.out.println("Main called launch");
	}

	public void start(Stage theStage) {
		System.out.println("Start method called.");

		BorderPane border = new BorderPane();
		System.out.println("BorderPane created");

		HBox hbox = new HBox();
		HBox topHBox = new HBox();
		System.out.println("HBox created");

		border.setTop(hbox);
		border.setPadding(new Insets(3, 10, 10, 10));

		hbox.setPadding(new Insets(0, 10, 0, 10));
		hbox.setSpacing(0);

		hbox.setAlignment(Pos.CENTER);

		topHBox.setPadding(new Insets(10, 10, 10, 10));
		topHBox.setSpacing(0);

		Label labelTwo = new Label("Time");

		int zeroDigit = 0;
		int firstDigit = alteredNumBombs / 10;
		int secondDigit = alteredNumBombs % 10;
		ImageView first = new ImageView();
		ImageView second = new ImageView();
		ImageView zero = new ImageView();
		first.setFitHeight(50);
		first.setFitWidth(25);
		second.setFitHeight(50);
		second.setFitWidth(25);
		zero.setFitHeight(50);
		zero.setFitWidth(25);
		first.setImage(new Image("file:res/digits/" + firstDigit + ".png"));
		second.setImage(new Image("file:res/digits/" + secondDigit + ".png"));
		zero.setImage(new Image("file:res/digits/0.png"));

		ImageView firstTimer = new ImageView();
		ImageView secondTimer = new ImageView();
		ImageView thirdTimer = new ImageView();
		firstTimer.setFitHeight(50);
		firstTimer.setFitWidth(25);
		secondTimer.setFitHeight(50);
		secondTimer.setFitWidth(25);
		thirdTimer.setFitHeight(50);
		thirdTimer.setFitWidth(25);
		firstTimer.setImage(new Image("file:res/digits/" + timerOne + ".png"));
		secondTimer.setImage(new Image("file:res/digits/" + timerTwo + ".png"));
		thirdTimer.setImage(new Image("file:res/digits/" + timerThree + ".png"));

		Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
			secondsElapsed++;
			timerOne = 0;
			if (secondsElapsed < 100) {
				timerTwo = secondsElapsed / 10;
			} else {
				timerOne = (int) ((secondsElapsed / 10) / 10) % 10;
				timerTwo = (int) (secondsElapsed / 10) % 10;
			}
			timerThree = (secondsElapsed % 10) % 10;
			//System.out.println(secondsElapsed);
			firstTimer.setImage(new Image("file:res/digits/" + timerOne + ".png"));
			secondTimer.setImage(new Image("file:res/digits/" + timerTwo + ".png"));
			thirdTimer.setImage(new Image("file:res/digits/" + timerThree + ".png"));
		}));
		timer.setCycleCount(999);
		timer.play();

		HBox facePane = new HBox();
		Button face = new Button();
		face.setGraphic(new ImageView(new Image("file:res/face-smile.png")));
		face.setMinWidth(50);
		face.setMinHeight(50);
		face.setMaxWidth(50);
		face.setMaxHeight(50);
		face.setPadding(new Insets(10));
		facePane.getChildren().addAll(face);
		facePane.setPadding(new Insets(10));
		System.out.println("Graphics and alignments set");

		hbox.getChildren().addAll(zero, first, second, facePane, firstTimer, secondTimer, thirdTimer);
		hbox.setAlignment(Pos.CENTER);
		System.out.println("Labels and FaceBTN added");
		hbox.setStyle("-fx-border-width:4px;" + "-fx-border-color: #7b7b7b #f6f6f6 #f6f6f6 #7b7b7b;"
				+ "-fx-border-radius: 3px;" + "-fx-background-color: #bdbdbd;");
		topHBox.getChildren().addAll(hbox);
		topHBox.setPadding(new Insets(10, 20, 10, 20));

		border.setTop(topHBox);
		border.setStyle("-fx-background-color: #bdbdbd;" + "-fx-border-width: 4px;"
				+ "-fx-border-color: #f6f6f6 #7b7b7b #7b7b7b #f6f6f6;" + "-fx-border-radius: 3px;");
		topHBox.setAlignment(Pos.CENTER);

		GridPane pane = new GridPane();

		pane.setStyle("-fx-border-color: #7b7b7b #f6f6f6 #f6f6f6 #7b7b7b;" + "-fx-border-width: 4px;"
				+ "-fx-border-radius: 3px;");
		border.setCenter(pane);

		// MinesweeperButton buttons[][] = new MinesweeperButton[5][5];
		// System.out.println("Preparing to set bombs and heatmap");
		setBombs();
		// System.out.println("Bombs set");
		setHeatMap();
		// System.out.println("Heatmap set");
		// System.out.println(grid[2][3] + "Value");
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				// System.out.println("Preparing Buttons");
				buttons[i][j] = new MinesweeperButton(i, j);
				if (grid[i][j] == 9) {
					buttons[i][j].state = 9;
				}
				if (grid[i][j] == 0) {
					buttons[i][j].state = 0;
				}
				if (grid[i][j] == 1) {
					buttons[i][j].state = 1;
				}
				if (grid[i][j] == 2) {
					buttons[i][j].state = 2;
				}
				if (grid[i][j] == 3) {
					buttons[i][j].state = 3;
				}
				if (grid[i][j] == 4) {
					buttons[i][j].state = 4;
				}
				if (grid[i][j] == 5) {
					buttons[i][j].state = 5;
				}
				MinesweeperButton b = buttons[i][j];
				// System.out.println("Buttons Created");
				String imagePath = "file:res/" + grid[i][j] + ".png";
				buttons[i][j].setGraphic(new ImageView(new Image("file:res/cover.png")));

				face.setOnMouseClicked(e -> {
					if (e.getButton().equals(MouseButton.PRIMARY)) {
						//System.out.println("Face Clicked");
						for (int row = 0; row < width; row++) {
							for (int col = 0; col < height; col++) {
								grid[row][col] = 0;
								heatmap[row][col] = 0;
								buttons[row][col].setGraphic(new ImageView(new Image("file:res/cover.png")));
							}
						}
						resetGame();
						for (int row = 0; row < width; row++) {
							for (int col = 0; col < height; col++) {
								String resetImagePath = "file:res/" + grid[row][col] + ".png";
								buttons[row][col].setGraphic(new ImageView(new Image("file:res/cover.png")));
							}
						}
						timer.stop();
						start(theStage);
					}

				});

				b.setOnMouseClicked(e -> {
					int ourX = b.x;
					int ourY = b.y;
					if (firstClick == 1) {
						if (grid[b.x][b.y] == 9) {
							if (e.getButton().equals(MouseButton.PRIMARY)) {
								while (grid[ourX][ourY] == 9) {
									timer.stop();
									for (int row = 0; row < width; row++) {
										for (int col = 0; col < height; col++) {
											grid[row][col] = 0;
											heatmap[row][col] = 0;
											buttons[row][col]
													.setGraphic(new ImageView(new Image("file:res/cover.png")));
										}
									}
									resetGameAddOne();
									resetCount++;
									System.out.println("reset");
									for (int row = 0; row < width; row++) {
										for (int col = 0; col < height; col++) {
											String resetImagePath = "file:res/" + grid[row][col] + ".png";
											buttons[row][col]
													.setGraphic(new ImageView(new Image("file:res/cover.png")));
										}
									}

									start(theStage);
								}
							}
						}
						firstClick++;
					}
					System.out.println(ourX + " " + ourY);
					if (e.getButton().equals(MouseButton.PRIMARY)) {
						if (b.isClicked == false && b.state != 9) {
							numGuessed++;
						}
						System.out.println("State: " + b.state);

						if (buttons[ourX][ourY].state == 9) {
							face.setGraphic(new ImageView(new Image("file:res/face-dead.png")));
							gameOver = true;
							buttons[ourX][ourY].isClicked = true;
							timer.stop();
							for (int x = 0; x < grid.length; x++) {
								for (int y = 0; y < grid[x].length; y++) {
									if (grid[x][y] == 9) {
										buttons[x][y].setGraphic(new ImageView(new Image("file:res/mine-grey.png")));
									}
									if (grid[x][y] != 9 && buttons[x][y].hasFlag) {
										buttons[x][y]
												.setGraphic(new ImageView(new Image("file:res/mine-misflagged.png")));
									}
									buttons[x][y].setDisable(true);
									buttons[x][y].setStyle("-fx-opacity: 1");
								}
							}

							buttons[ourX][ourY].setGraphic(new ImageView(new Image("file:res/mine-red.png")));
						}
						if (buttons[ourX][ourY].state == 0 && !buttons[ourX][ourY].hasFlag) {
							setImage(buttons[ourX][ourY], "file:res/" + grid[ourX][ourY] + ".png");
							revealZero(buttons, ourX, ourY);
							// b.isClicked = true;
						}
						if (buttons[ourX][ourY].state >= 1 && buttons[ourX][ourY].state <= 5
								&& !buttons[ourX][ourY].hasFlag) {
							setImage(buttons[ourX][ourY], "file:res/" + grid[ourX][ourY] + ".png");
							buttons[ourX][ourY].isClicked = true;
						}
					} else if (e.getButton().equals(MouseButton.SECONDARY)) {
						if (buttons[ourX][ourY].hasFlag == false && !(buttons[ourX][ourY].isClicked) ) {
							buttons[ourX][ourY].hasFlag = true;
							alteredNumBombs--;
							buttons[ourX][ourY].setGraphic(new ImageView(new Image("file:res/flag.png")));
						} else if (buttons[ourX][ourY].hasFlag == true) {
							buttons[ourX][ourY].hasFlag = false;
							alteredNumBombs++;
							buttons[ourX][ourY].setGraphic(new ImageView(new Image("file:res/cover.png")));
						}
						
						
						int adjacentFlagCount = checkAdjacentFlags(buttons, grid, b.x, b.y);
						
						if (grid[b.x][b.y] == adjacentFlagCount) {
							revealSurrounding(buttons, ourX, ourY);
							System.out.println("Revealed surrounding tiles");
						}

						int x = alteredNumBombs / 10;
						int y = alteredNumBombs % 10;
						first.setImage(new Image("file:res/digits/" + x + ".png"));
						second.setImage(new Image("file:res/digits/" + y + ".png"));
						
						if (gameOver == true) {
							face.setGraphic(new ImageView(new Image("file:res/face-dead.png")));
							gameOver = true;
							buttons[ourX][ourY].isClicked = true;
							timer.stop();
							for (int row = 0; row < grid.length; row++) {
								for (int col = 0; col < grid[row].length; col++) {
									if (grid[row][col] == 9) {
										buttons[row][col].setGraphic(new ImageView(new Image("file:res/mine-grey.png")));
									}
									if (grid[row][col] != 9 && buttons[row][col].hasFlag) {
										buttons[row][col]
												.setGraphic(new ImageView(new Image("file:res/mine-misflagged.png")));
									}
									buttons[row][col].setDisable(true);
									buttons[row][col].setStyle("-fx-opacity: 1");
								}
							}
						}
						
					}

					if (winGame(buttons) == true) {
						face.setGraphic(new ImageView(new Image("file:res/face-win.png")));
						for (int x = 0; x < grid.length; x++) {
							for (int y = 0; y < grid[x].length; y++) {
								buttons[x][y].setDisable(true);
								buttons[x][y].setStyle("-fx-opacity: 1");
								System.out.println("Winner!");
								timer.stop();
							}
						}
					}

				});
				pane.add(buttons[i][j], i, j);
			}
		}

		theStage.setTitle("Minesweeper");
		theStage.getIcons().add(new Image("file:res/face-smile.png"));
		theStage.setScene(new Scene(border));
		theStage.show();

	}
	
	public static int checkAdjacentFlags(MinesweeperButton[][] buttons, int[][] grid, int x, int y) {
        int adjacentFlagCount = 0;
        //int[][] surroundingAreaGrid = new int[3][3];
        boolean [][] surroundingAreaButtons = new boolean[3][3];
        
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++) {
                if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length) {
                    surroundingAreaButtons[i-x+1][j-y+1] = buttons[i][j].hasFlag;
                } else {
                    surroundingAreaButtons[i-x+1][j-y+1] = false;
                }
            }
        }
        
        // testing purposes
        for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				System.out.print(surroundingAreaButtons[row][col] + " ");
			}
			System.out.println();
		}
        //
        
        for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if (surroundingAreaButtons[row][col] == true) {
					adjacentFlagCount++;
				}
			}
			System.out.println();
		}
        
        return adjacentFlagCount;
    }

	public static void revealSurrounding(MinesweeperButton[][] buttons, int x, int y) {
		System.out.println("Passed base case");
		if (buttons[x][y].isClicked == false) {
			numGuessed++;
		}
		System.out.println("X: " + x + "   Y: " + y);

		buttons[x][y].isClicked = true;
		
		if (x != width - 1) {
			if (!(buttons[x + 1][y].hasFlag)) {
				setImage(buttons[x+1][y], "file:res/" + grid[x+1][y] + ".png");
				buttons[x+1][y].isClicked = true;
				if (grid[x + 1][y] == 9) {
					setImage(buttons[x+1][y], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("x+1y");
			}
		}		
		if (x != 0) {
			if (!buttons[x - 1][y].hasFlag) {
				setImage(buttons[x-1][y], "file:res/" + grid[x-1][y] + ".png");
				buttons[x-1][y].isClicked = true;
				if (grid[x - 1][y] == 9) {
					setImage(buttons[x-1][y], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("x-1y");
			}
		}
		if (y != height - 1) {
			if (!buttons[x][y + 1].hasFlag) {
				setImage(buttons[x][y+1], "file:res/" + grid[x][y+1] + ".png");
				buttons[x][y+1].isClicked = true;
				if (grid[x][y +1] == 9) {
					setImage(buttons[x+1][y+1], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("xy+1");
			}
		}
		if (y != 0) {
			if (!buttons[x][y-1].hasFlag) {
				setImage(buttons[x][y-1], "file:res/" + grid[x][y-1] + ".png");
				buttons[x][y-1].isClicked = true;
				if (grid[x][y -1] == 9) {
					setImage(buttons[x][y-1], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("xy-1");
			}
		}
		
		if ((x != width - 1) && (y != height - 1)) {
			if (!buttons[x + 1][y + 1].hasFlag) {
				setImage(buttons[x+1][y+1], "file:res/" + grid[x+1][y+1] + ".png");
				buttons[x+1][y+1].isClicked = true;
				if (grid[x + 1][y +1] == 9) {
					setImage(buttons[x+1][y+1], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("x+1y+1");
			}
		}
		if ((x != 0) && (y != 0)) {
			if (!buttons[x - 1][y - 1].hasFlag){
				setImage(buttons[x-1][y-1], "file:res/" + grid[x-1][y-1] + ".png");
				buttons[x-1][y-1].isClicked = true;
				if (grid[x -1 ][y -1] == 9) {
					setImage(buttons[x-1][y-1], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("x-1y-1");
			}
		}
		if ((y > 0) && (x != height - 1)) {
			if (!buttons[x + 1][y -1].hasFlag) {
				setImage(buttons[x+1][y-1], "file:res/" + grid[x+1][y-1] + ".png");
				buttons[x+1][y-1].isClicked = true;
				if (grid[x + 1][y -1] == 9) {
					setImage(buttons[x+1][y-1], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("x+1y-1");
			}
		}
		if ((x != 0) && (y != height - 1)) {
			if (!buttons[x - 1][y + 1].hasFlag) {
				setImage(buttons[x-1][y+1], "file:res/" + grid[x-1][y+1] + ".png");
				buttons[x-1][y+1].isClicked = true;
				if (grid[x - 1][y +1] == 9) {
					setImage(buttons[x-1][y+1], "file:res/mine_red.png");
					gameOver = true;
				}
				//System.out.println("x-1y+1");
			}
		}		
	}

	public static void revealZero(MinesweeperButton[][] buttons, int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height || buttons[x][y].isClicked) {
			return;
		}

		if (buttons[x][y].isClicked == false) {
			numGuessed++;
		}

		buttons[x][y].isClicked = true;
		String imagePath = "file:res/" + grid[x][y] + ".png";

		if (buttons[x][y].state != 0) {
			setImage(buttons[x][y], imagePath);
			return;
		}

		setImage(buttons[x][y], imagePath);

		revealZero(buttons, x - 1, y - 1);
		revealZero(buttons, x - 1, y);
		revealZero(buttons, x - 1, y + 1);
		revealZero(buttons, x, y - 1);
		revealZero(buttons, x, y + 1);
		revealZero(buttons, x + 1, y - 1);
		revealZero(buttons, x + 1, y);
		revealZero(buttons, x + 1, y + 1);
	}

	public static void setImage(MinesweeperButton b, String imagePath) {
		b.setGraphic(new ImageView(new Image(imagePath)));
	}

	public static void setBombs() {
		boolean modified = false;
		System.out.println("Setting bombs...");

		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = 0;
			}
		}

		Random random = new Random();

		for (int i = 0; i < numBombs; i++) {
			int x = (int) random.nextInt(7);
			System.out.println(x);
			int y = (int) random.nextInt(7);
			System.out.println(y);

			while (modified == false) {
				if (i == 0) {
					grid[x][y] = 9;
					modified = true;
				}
				if (!(grid[x][y] == 9)) {
					grid[x][y] = 9;
					modified = true;
					// System.out.println("Set to nine");
				}
				// System.out.println("Duplicate");
				x = (int) random.nextInt(7);
				// System.out.println(x);
				y = (int) random.nextInt(7);
				// System.out.println(y);
			}
			// System.out.println(i);
			modified = false;

		}

	}

	public void resetGame() {
		// reset buttons
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buttons[i][j].reset();
			}
		}

		numGuessed = 0;
		gameOver = false;
		reset = true;
		firstClick = 1;
		numTiles = size * size;
		numBombs = 10;
		alteredNumBombs = 10;
		numSafe = numTiles - numBombs;
		secondsElapsed = 0;
		timerOne = 0;
		timerTwo = 0;
		timerThree = 0;
		tilesCleared = 0;
		setBombs();
		setHeatMap();
		resetCount = 0;
	}

	public void resetGameAddOne() {
		// reset buttons
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buttons[i][j].reset();
			}
		}

		numGuessed = 0;
		gameOver = false;
		reset = true;
		firstClick = 2;
		numTiles = size * size;
		numBombs = 10;
		alteredNumBombs = 10;
		numSafe = numTiles - numBombs;
		secondsElapsed = 0;
		timerOne = 0;
		timerTwo = 0;
		timerThree = 0;
		tilesCleared = 0;
		setBombs();
		setHeatMap();
	}

	public static void setHeatMap() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (grid[i][j] == 9) {
					for (int k = i - 1; k <= i + 1; k++) {
						for (int l = j - 1; l <= j + 1; l++) {
							if (k >= 0 && k < grid.length && l >= 0 && l < grid[k].length && grid[k][l] != 9) {
								grid[k][l]++;
							}
						}
					}
				}
			}
		}
	}

	public static boolean winGame(MinesweeperButton[][] buttons) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (buttons[i][j].isClicked) {
					tilesCleared++;
				}
				if (buttons[i][j].hasFlag && buttons[i][j].isClicked) {
					tilesCleared--;
				}
			}
		}
		System.out.println("Tiles Cleared:" + tilesCleared + "\n" + "Num Safe:" + numSafe);
		if (tilesCleared == numSafe) {
			gameOver = true;
			return true;
		}
		tilesCleared = 0;
		return false;
	}
}

class MinesweeperButton extends Button {
	int state;
	int x;
	int y;
	ImageView imageCover, imageZero, imageOne, imageTwo, imageThree, imageFour, imageFive;
	boolean isClicked = false;
	boolean hasFlag = false;
	boolean firstClick = true;

	public MinesweeperButton(int x, int y) {

		this.x = x;
		this.y = y;
		state = 0;
		double size = 35;
		setMinWidth(size);
		setMinHeight(size);
		setMaxHeight(size);
		setMaxWidth(size);

		imageCover = new ImageView(new Image("file:res/cover.png"));
		imageZero = new ImageView(new Image("file:res/0.png"));
		imageOne = new ImageView(new Image("file:res/1.png"));
		imageTwo = new ImageView(new Image("file:res/2.png"));
		imageThree = new ImageView(new Image("file:res/3.png"));
		imageFour = new ImageView(new Image("file:res/4.png"));
		imageFive = new ImageView(new Image("file:res/5.png"));

		imageCover.setFitWidth(size);
		imageCover.setFitHeight(size);
		imageOne.setFitWidth(size);
		imageOne.setFitHeight(size);
		imageTwo.setFitWidth(size);
		imageTwo.setFitHeight(size);
		setStyle("-fx-border-color: #C0C0C0");
	}

	public void reset() {
		x = 0;
		y = 0;
		hasFlag = false;
		state = 0;
	}
}
