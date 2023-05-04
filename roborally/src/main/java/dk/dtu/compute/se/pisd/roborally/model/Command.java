/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.

    FORWARD("Move 1"),
    FAST_FORWARD("Move 2"),
    LEFT("Turn Left"),
    RIGHT("Turn Right"),
    SPRINT_FORWARD("Move 3"),
    BACK_UP("Back up"),
    U_TURN("U-turn"),
    AGAIN("Again"),

    MOVELEFT("Move Left"),
    MOVERIGHT("Move Right"),

    POWER_UP("Power up"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT),

    //SPAM CARDS

    SPAM ("Spam"),

    WORM ("Worm"),

    TROJAN_HORSE("Trojan horse"),

    VIRUS("Virus"),

    //UPGRADE CARDS
    RAMMING_GEAR_PUPG("Ramming Gear - text here"),
    SPAM_BLOCKER_TUPG("Spam Blocker \n Replace each SPAM damage card in your hand with a card from the top of your deck. Permanently discard the SPAM damage cards"),
    ENERGY_ROUTINE_TUPG("Energy Routine \n Add the Energy Routine Programming card to your discard pile"),
    SPAM_FOLDER_TUPG("Spam Folder Routine \n"),
    SPAM_FOLDER("Spam folder"),
    SANDBOX("Sandbox Routine", FORWARD, FAST_FORWARD, SPRINT_FORWARD, BACK_UP, LEFT, RIGHT, U_TURN),
    WEASEL("Weasel routine", LEFT, RIGHT, U_TURN),
    SPEED("Speed routine"),
    ENERGY("Energy routine"),
    RECOMPILE_TUPG("Recompile \n Discard your entire hand. Draw a new one"),
    RECHARGE_TUPG("Recharge \n Gain three energy"),
    HACK_TUPG("Hack \n Execute the programming in your current register again"),
    ZOOP_TUPG("Zoop \n Rotate to face any direction", LEFT, RIGHT, U_TURN),
    REBOOT_TUPG("Reboot \n Reboot your robot, but take no damage"),
    REPEAT_ROUTINE_TUPG("Repeat Routine \n Add the Repeat Routine Programming card to your discard pile"),
    DEFRAG_GIZMO_PUPG("Defrag Gizmo \n Permanently discards a damage card from your hand"),
    BOINK_TUPG("Boink \n \n Move to an adjacent space. Do not change direction", FORWARD, BACK_UP, MOVELEFT, MOVERIGHT);
    // XXX Assignment P3

    final public String displayName;

    // XXX Assignment P3
    // Command(String displayName) {
    //     this.displayName = displayName;
    // }
    //
    // replaced by the code below:

    final private List<Command> options;

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    public boolean isInteractive() {
        return !options.isEmpty();
    }

    public List<Command> getOptions() {
        return options;
    }

}

