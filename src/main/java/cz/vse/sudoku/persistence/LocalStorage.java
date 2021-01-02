package cz.vse.sudoku.persistence;

import cz.vse.sudoku.logic.Cell;
import javafx.stage.FileChooser;

import java.io.*;

public class LocalStorage implements PersistenceProvider {
    private static final String SAVE_FILE_NAME = "sudoku_save.bin";

    @Override
    public Cell[][] loadGame() throws PersistenceException {
        try {
            File myFile = new File(SAVE_FILE_NAME);
            File dir = new File(myFile.getAbsoluteFile().getParent());

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".bin files", "*.bin");

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialDirectory(dir);

            File chosenFile = fileChooser.showOpenDialog(null);

            if (chosenFile != null) {
                FileInputStream fileInputStream = new FileInputStream(chosenFile.getPath());
                ObjectInputStream in = new ObjectInputStream(fileInputStream);

                Cell[][] cells = (Cell[][]) in.readObject();

                in.close();
                fileInputStream.close();

                return cells;
            }

            return null;
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void saveGame(Cell[][] cells) throws PersistenceException {
        try {
            File myFile = new File(SAVE_FILE_NAME);
            File dir = new File(myFile.getAbsoluteFile().getParent());

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("All files", "*.*");
            String saveFileName = "sudoku_save_" + System.currentTimeMillis() + ".bin";

            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(saveFileName);
            fileChooser.setInitialDirectory(dir);

            File chosenFile = fileChooser.showSaveDialog(null);

            if (chosenFile != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(chosenFile.getPath());
                ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
                out.writeObject(cells);

                out.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            throw new PersistenceException(e);
        }
    }
}
