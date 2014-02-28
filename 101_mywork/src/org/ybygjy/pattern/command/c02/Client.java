package org.ybygjy.pattern.command.c02;

import org.ybygjy.pattern.command.c02.impl.MacroAudioCommand;
import org.ybygjy.pattern.command.c02.impl.PlayCommand;
import org.ybygjy.pattern.command.c02.impl.RewindCommand;
import org.ybygjy.pattern.command.c02.impl.StopCommand;

public class Client {
    private void generalTest() {
        //������
        AudioPlayer audioPlayer = new AudioPlayer();
        //����
        Command stopCommand = new StopCommand(audioPlayer);
        Command rewindCommand = new RewindCommand(audioPlayer);
        Command playCommand = new PlayCommand(audioPlayer);
        //������
        Keypad keyPad = new Keypad(stopCommand, rewindCommand, playCommand);
        keyPad.play();
    }
    private void macroTest() {
        AudioPlayer audioPlayer = new AudioPlayer();
        Command rewindCommand = new RewindCommand(audioPlayer);
        Command playCommand = new PlayCommand(audioPlayer);
        MacroCommand macroCommand = new MacroAudioCommand();
        macroCommand.add(rewindCommand);
        macroCommand.add(playCommand);
        macroCommand.execute();
    }
    public static void main(String[] args) {
        new Client().generalTest();
        new Client().macroTest();
    }
}
