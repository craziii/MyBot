package com.evilduck;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.client.requests.restaction.pagination.MentionPaginationAction;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.GuildVoiceState;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.MessageReaction;
import net.dv8tion.jda.core.entities.MessageType;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.entities.Webhook;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.GuildManager;
import net.dv8tion.jda.core.managers.GuildManagerUpdatable;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import net.dv8tion.jda.core.requests.restaction.pagination.AuditLogPaginationAction;
import net.dv8tion.jda.core.utils.cache.MemberCacheView;
import net.dv8tion.jda.core.utils.cache.SnowflakeCacheView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.OffsetDateTime;
import java.util.Formatter;
import java.util.List;
import java.util.Set;

public class TestMessage implements Message {

    private List<User> mentionedUsers;
    private String contentRaw;

    public void setMentionedUsers(List<User> mentionedUsers) {
        this.mentionedUsers = mentionedUsers;
    }

    @Override
    public List<User> getMentionedUsers() {
        return null;
    }

    @Override
    public List<TextChannel> getMentionedChannels() {
        return null;
    }

    @Override
    public List<Role> getMentionedRoles() {
        return null;
    }

    @Override
    public List<Member> getMentionedMembers(Guild guild) {
        return null;
    }

    @Override
    public List<Member> getMentionedMembers() {
        return null;
    }

    @Override
    public List<IMentionable> getMentions(MentionType... types) {
        return null;
    }

    @Override
    public boolean isMentioned(IMentionable mentionable, MentionType... types) {
        return false;
    }

    @Override
    public boolean mentionsEveryone() {
        return false;
    }

    @Override
    public boolean isEdited() {
        return false;
    }

    @Override
    public OffsetDateTime getEditedTime() {
        return null;
    }

    @Override
    public User getAuthor() {
        return null;
    }

    @Override
    public Member getMember() {
        return null;
    }

    @Override
    public String getContentDisplay() {
        return null;
    }

    public void setContentRaw(String contentRaw) {
        this.contentRaw = contentRaw;
    }

    @Override
    public String getContentRaw() {
        return null;
    }

    @Override
    public String getContentStripped() {
        return null;
    }

    @Override
    public List<String> getInvites() {
        return null;
    }

    @Override
    public String getNonce() {
        return null;
    }

    @Override
    public boolean isFromType(ChannelType type) {
        return false;
    }

    @Override
    public ChannelType getChannelType() {
        return null;
    }

    @Override
    public boolean isWebhookMessage() {
        return false;
    }

    @Override
    public MessageChannel getChannel() {
        return null;
    }

    @Override
    public PrivateChannel getPrivateChannel() {
        return null;
    }

    @Override
    public Group getGroup() {
        return null;
    }

    @Override
    public TextChannel getTextChannel() {
        return null;
    }

    @Override
    public Category getCategory() {
        return null;
    }

    @Override
    public Guild getGuild() {
        return null;
    }

    @Override
    public List<Attachment> getAttachments() {
        return null;
    }

    @Override
    public List<MessageEmbed> getEmbeds() {
        return null;
    }

    @Override
    public List<Emote> getEmotes() {
        return null;
    }

    @Override
    public List<MessageReaction> getReactions() {
        return null;
    }

    @Override
    public boolean isTTS() {
        return false;
    }

    @Override
    public MessageAction editMessage(CharSequence newContent) {
        return null;
    }

    @Override
    public MessageAction editMessage(MessageEmbed newContent) {
        return null;
    }

    @Override
    public MessageAction editMessageFormat(String format, Object... args) {
        return null;
    }

    @Override
    public MessageAction editMessage(Message newContent) {
        return null;
    }

    @Override
    public AuditableRestAction<Void> delete() {
        return null;
    }

    @Override
    public JDA getJDA() {
        return null;
    }

    @Override
    public boolean isPinned() {
        return false;
    }

    @Override
    public RestAction<Void> pin() {
        return null;
    }

    @Override
    public RestAction<Void> unpin() {
        return null;
    }

    @Override
    public RestAction<Void> addReaction(Emote emote) {
        return null;
    }

    @Override
    public RestAction<Void> addReaction(String unicode) {
        return null;
    }

    @Override
    public RestAction<Void> clearReactions() {
        return null;
    }

    @Override
    public MessageType getType() {
        return null;
    }

    @Override
    public void formatTo(Formatter formatter, int flags, int width, int precision) {

    }

    @Override
    public long getIdLong() {
        return 0;
    }

    private class TestGuild implements Guild {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getIconId() {
            return null;
        }

        @Override
        public String getIconUrl() {
            return null;
        }

        @Override
        public Set<String> getFeatures() {
            return null;
        }

        @Override
        public String getSplashId() {
            return null;
        }

        @Override
        public String getSplashUrl() {
            return null;
        }

        @Override
        public RestAction<String> getVanityUrl() {
            return null;
        }

        @Override
        public VoiceChannel getAfkChannel() {
            return null;
        }

        @Override
        public TextChannel getSystemChannel() {
            return null;
        }

        @Override
        public Member getOwner() {
            return null;
        }

        @Override
        public Timeout getAfkTimeout() {
            return null;
        }

        @Override
        public String getRegionRaw() {
            return null;
        }

        @Override
        public boolean isMember(User user) {
            return false;
        }

        @Override
        public Member getSelfMember() {
            return null;
        }

        @Override
        public Member getMember(User user) {
            return null;
        }

        @Override
        public MemberCacheView getMemberCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<Category> getCategoryCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<TextChannel> getTextChannelCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<VoiceChannel> getVoiceChannelCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<Role> getRoleCache() {
            return null;
        }

        @Override
        public SnowflakeCacheView<Emote> getEmoteCache() {
            return null;
        }

        @Override
        public RestAction<List<User>> getBans() {
            return null;
        }

        @Nonnull
        @Override
        public RestAction<List<Ban>> getBanList() {
            return null;
        }

        @Override
        public RestAction<Integer> getPrunableMemberCount(int days) {
            return null;
        }

        @Override
        public Role getPublicRole() {
            return null;
        }

        @Nullable
        @Override
        public TextChannel getDefaultChannel() {
            return null;
        }

        @Override
        public GuildManager getManager() {
            return null;
        }

        @Override
        public GuildManagerUpdatable getManagerUpdatable() {
            return null;
        }

        @Override
        public GuildController getController() {
            return null;
        }

        @Override
        public MentionPaginationAction getRecentMentions() {
            return null;
        }

        @Override
        public AuditLogPaginationAction getAuditLogs() {
            return null;
        }

        @Override
        public RestAction<Void> leave() {
            return null;
        }

        @Override
        public RestAction<Void> delete() {
            return null;
        }

        @Override
        public RestAction<Void> delete(String mfaCode) {
            return null;
        }

        @Override
        public AudioManager getAudioManager() {
            return null;
        }

        @Override
        public JDA getJDA() {
            return null;
        }

        @Override
        public RestAction<List<Invite>> getInvites() {
            return null;
        }

        @Override
        public RestAction<List<Webhook>> getWebhooks() {
            return null;
        }

        @Override
        public List<GuildVoiceState> getVoiceStates() {
            return null;
        }

        @Override
        public VerificationLevel getVerificationLevel() {
            return null;
        }

        @Override
        public NotificationLevel getDefaultNotificationLevel() {
            return null;
        }

        @Override
        public MFALevel getRequiredMFALevel() {
            return null;
        }

        @Override
        public ExplicitContentLevel getExplicitContentLevel() {
            return null;
        }

        @Override
        public boolean checkVerification() {
            return false;
        }

        @Override
        public boolean isAvailable() {
            return false;
        }

        @Override
        public long getIdLong() {
            return 0;
        }
    }
}
