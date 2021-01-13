package me.marcarrots.speller;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Redo implements TabExecutor {

    private final Speller speller;

    public Redo(Speller speller) {
        this.speller = speller;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (! (sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if (!speller.getRedoStack(player.getUniqueId()).isEmpty()) {
            ArrayList<Task> task = speller.getRedoStack(player.getUniqueId()).pop();
            for (Task t: task) {
                Block block = t.getWorld().getBlockAt(t.getX(), t.getY(), t.getZ());
                final Material materialOld = t.getMaterialOld();
                t.setMaterialOld(block.getType());
                block.setType(materialOld);
            }
            speller.addUndo(task, player.getUniqueId());
            sender.sendMessage(String.format("Redid %d blocks.", task.size()));

        } else {
            sender.sendMessage("Nothing to redo.");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
