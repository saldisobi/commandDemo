package android.template.ui.mymodel

interface CommandReceiver {
    fun onAddClicked()
    fun onTextUpdate(newText: String)
    fun onDeleteClicked()
    fun onListClicked()
    fun onSaveClicked(text: String)
    fun processCommand(command: Command) {
        command.execute(this)
    }
}

interface Command {
    fun execute(receiver: CommandReceiver)
}

class AddCommand : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onAddClicked()
    }
}

class TextUpdateCommand(private val newText: String) : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onTextUpdate(newText)
    }
}

class DeleteProductCommand : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onDeleteClicked()
    }
}

class ListCommand() : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onListClicked()
    }
}


class SaveCommand(private val text: String) : Command {
    override fun execute(receiver: CommandReceiver) {
        receiver.onSaveClicked(text)
    }
}