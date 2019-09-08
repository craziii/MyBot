package com.evilduck.command;

import com.evilduck.command.interfaces.IsACommand;
import com.evilduck.command.interfaces.PublicCommand;
import com.evilduck.command.interfaces.UnstableCommand;
import com.evilduck.entity.GraphicsTransformer;
import com.evilduck.util.CommandHelper;
import com.jecklgamis.util.TryFactory;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.PI;

@Component
@IsACommand(description = "Manipulates images for fun!", tutorial = "Use !image with a keyword command and attach an image to modify it", aliases = "i")
public class Image implements PublicCommand, UnstableCommand {

    private final GraphicsTransformer graphicsTransformer;
    private final CommandHelper commandHelper;

    @Autowired
    public Image(final GraphicsTransformer graphicsTransformer,
                 final CommandHelper commandHelper) {
        this.graphicsTransformer = graphicsTransformer;
        this.commandHelper = commandHelper;
    }

    @Override
    @ServiceActivator(inputChannel = "imageChannel")
    public void execute(final Message message) throws IOException {
        final List<String> args = commandHelper.getArgs(message.getContentRaw());
        final TextChannel originChannel = message.getTextChannel();
        if (args.isEmpty()) {
            originChannel.sendMessage("You haven't specified what I should do with the image!").queue();
            return;
        }

        final Optional<Message.Attachment> attachedImage = getAttachedImage(message);
        if (attachedImage.isPresent()) {
            final Optional<File> transformedImage = performTransform(
                    attachedImage.get(),
                    new TransformType(args.get(0), args),
                    originChannel);
            if (transformedImage.isPresent()) {
                originChannel.sendFile(transformedImage.get(), "transformed.png")
                        .queue((success) -> {
                        }, failure -> originChannel.sendMessage("I was unable to perform your transform! Reason: " + failure.getMessage()));
                {

                }
                ;
            } else originChannel.sendMessage("I don't recognise that image command").queue();
        } else originChannel.sendMessage("No image attached!").queue();
    }

    private Optional<File> performTransform(final Message.Attachment attachedImage,
                                            final TransformType transform,
                                            final MessageChannel originChannel) throws IOException {
        final BufferedImage image = ImageIO.read(attachedImage.getInputStream());
        if (transform.isRotate() && transform.canRotate()) {
            final double tau = PI * 2;
            final Double rotation = transform.getRotation();
            final boolean isDegreesRotation = rotation > tau;
            originChannel.sendMessage("Assuming rotation is in " + (isDegreesRotation ? "degrees" : "radians")).queue();
            final double theta = isDegreesRotation ? Math.toRadians(rotation) : rotation;
            return Optional.of(graphicsTransformer.rotateImage(image, theta));
        } else return Optional.empty();
    }

    private static Optional<Message.Attachment> getAttachedImage(final Message message) {
        return message.getAttachments().stream().findFirst();
    }

    private class TransformType {

        private final String plannedTransform;
        private final List<?> transformArgs;

        private TransformType(final String plannedTransform,
                              final List<?> transformArgs) {
            this.plannedTransform = plannedTransform;
            this.transformArgs = transformArgs;
        }

        public boolean isRotate() {
            return plannedTransform.matches("rotate|turn");
        }

        public boolean canRotate() {
            return transformArgs.size() > 1 && (((String) transformArgs.get(1)).matches("pi|tau") ||
                    TryFactory.attempt(() -> (Double.parseDouble((String) transformArgs.get(1)))).isSuccess());
        }

        public Double getRotation() {
            return TryFactory.attempt(() -> Double.parseDouble((String) transformArgs.get(1)))
                    .getOrElse(() -> ((String) transformArgs.get(1)).matches("pi") ? PI :
                            ((String) transformArgs.get(1)).matches("tau") ? (PI * 2) : 0);
        }

    }

}
