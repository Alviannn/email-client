package dev.gamavi.emailclient.menu;

import dev.gamavi.emailclient.model.Mail;
import dev.gamavi.emailclient.model.MailRecipient;
import dev.gamavi.emailclient.model.User;
import dev.gamavi.emailclient.service.MailService;
import dev.gamavi.emailclient.shared.Shared;
import dev.gamavi.emailclient.shared.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ForwardMailMenu extends AbstractMenu {

    private Mail clonedMail;

    private final Shared shared = Shared.getInstance();

    private final MailService mailService = shared.getMailService();

    @Override
    public void show() {
        Utils.clearScreen();

        Scanner scanner = Utils.SCANNER;

        System.out.print(
                "Forward Mail Menu\n" +
                "-----------------------\n");
        User currentUser = shared.getCurrentUser();
        List<MailRecipient> recipientList = new ArrayList<>();

        try {
            this.mailService.scanRecipients(scanner, recipientList);
        } catch (Exception e) {
            // forced exception to indicate on cancelling the whole menu
            // or going 'back'
            return;
        }

        boolean shouldSend = Utils.scanAbsoluteConfirm("Are you sure that you want to forward this email? [Y/N]: ");
        if (!shouldSend) {
            return;
        }

        clonedMail.setSender(currentUser);
        clonedMail.setTitle("FW: " + clonedMail.getTitle());
        mailService.composeAndSend(clonedMail, recipientList);
    }

    public void setClonedMail(Mail clonedMail) {
        this.clonedMail = clonedMail;
    }
}
