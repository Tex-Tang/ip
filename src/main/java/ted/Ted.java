package ted;

import ted.command.Command;
import ted.exception.InvalidEncodingException;
import ted.exception.TedException;
import ted.task.TaskList;

public class Ted {

    private TaskList tasks;

    private Storage storage;

    private Ui ui;

    public Ted(Ui ui, Storage storage) {
        this.ui = ui;
        this.storage = storage;
    }

    private void startup() {
        this.ui.showGreeting();
        try {
            this.tasks = storage.loadTasks();
            this.ui.showTaskLoadSuccess(this.tasks.size());
        } catch (InvalidEncodingException e) {
            this.ui.showTaskLoadError();
        }

        boolean isExit = false;
        while (!isExit) {
            try {
                String input = this.ui.promptInput();
                Command command = Parser.parse(input);
                command.run(tasks, ui, storage);
                isExit = command.isExit();
            } catch (TedException e) {
                this.ui.showInputError(e);
            }
        }

    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage("./data/tasks.txt");
        Ted bot = new Ted(ui, storage);
        bot.startup();
    }
}
