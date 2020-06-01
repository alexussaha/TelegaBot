import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendChatAction;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());

        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }





    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);


        // message.getMediaGroupId()

        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(text);
        try {

            setButtons(sendMessage);
            sendMessage(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public  void ResendFromVk(Message message){
        try {
            while (true){

                Thread.sleep(6000);
                String text;
                FileReader fileReader = new FileReader("C:\\Users\\usov_\\OneDrive\\Рабочий стол\\JavaProjectVkTelegaBot\\VKBOTNB\\db.txt");
                BufferedReader reader = new BufferedReader(fileReader);
                text = reader.readLine();
                if(text != null && text != " " && text != "") {
                    sendMsg(message, text);
                }


                fileReader.close();
                try(FileWriter writer = new FileWriter("C:\\Users\\usov_\\OneDrive\\Рабочий стол\\JavaProjectVkTelegaBot\\VKBOTNB\\db.txt", false))
                {
                    // запись всей строки
                    String emptyText = "";
                    writer.write(emptyText);


                    writer.flush();
                }
                catch(IOException ex){

                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private Thread newThread = null;

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case "/setting":

                    sendMsg(message, "Что будем настраивать?");
                    break;
                case "/resendVk":
                     newThread = new Thread(() -> {
                        ResendFromVk(message);
                    });
                    newThread.start();

                    break;
                case "/stop":
                    if(newThread!=null) {
                        System.out.println("stopped");
                        newThread.stop();
                    }
                    break;

                default:


            }
        }


    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));
        keyboardFirstRow.add(new KeyboardButton("/resendVk"));
        keyboardFirstRow.add(new KeyboardButton("/stop"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "AlexussahaFirstbot";
    }

    public String getBotToken() {
        return "1279105789:AAHCZJaBVf3-nzWRrs-CU-56xE2Q1Mfw4Co";
    }
}
