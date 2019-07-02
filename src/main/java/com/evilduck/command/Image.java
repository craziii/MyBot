package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.entity.GraphicsTransformer;
import net.dv8tion.jda.core.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Component
@IsACommand(aliases = "i")
public class Image implements PublicCommand, UnstableCommand {

    private final GraphicsTransformer graphicsTransformer;

    @Autowired
    public Image(final GraphicsTransformer graphicsTransformer) {
        this.graphicsTransformer = graphicsTransformer;
    }

    @Override
    @ServiceActivator(inputChannel = "imageChannel")
    public void execute(final Message message) throws IOException {
        Optional<Message.Attachment> attachedImage = getAttachedImage(message);
        if (attachedImage.isPresent()) {
            final InputStream inputStream = attachedImage.get().getInputStream();
            final BufferedImage image = ImageIO.read(inputStream);
            final File transformedImage = graphicsTransformer.rotateImage(image, Math.PI / 2);
            message.getTextChannel().sendFile(transformedImage, "transformed.png").queue();
        } else message.getTextChannel().sendMessage("No image attached!").queue();
    }

    private static final Optional<Message.Attachment> getAttachedImage(final Message message) {
        final Optional<Message.Attachment> attachment = message.getAttachments().stream().findFirst();
        return attachment;
    }

    @Override
    public void onSuccess(final Message message) {

    }

    @Override
    public void onFail(final Throwable throwable) {

    }
}
