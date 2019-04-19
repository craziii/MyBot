package com.evilduck.util;

public class Permission {

    private final boolean kickMembers;
    private final boolean banMembers;
    private final boolean moveMembers;
    private final boolean muteMembers;
    private final boolean deafenMembers;
    private final boolean manageNicks;
    private final boolean manageMessages;
    private final boolean addReaction;
    private final boolean mentionEveryone;
    private final boolean administrator;
    private final boolean embedLinks;
    private final boolean readHistory;
    private final boolean attachFiles;
    private final boolean unknown;

    public boolean isKickMembers() {
        return kickMembers;
    }

    public boolean isBanMembers() {
        return banMembers;
    }

    public boolean isMoveMembers() {
        return moveMembers;
    }

    public boolean isMuteMembers() {
        return muteMembers;
    }

    public boolean isDeafenMembers() {
        return deafenMembers;
    }

    public boolean isManageNicks() {
        return manageNicks;
    }

    public boolean isManageMessages() {
        return manageMessages;
    }

    public boolean isAddReaction() {
        return addReaction;
    }

    public boolean isMentionEveryone() {
        return mentionEveryone;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public boolean isEmbedLinks() {
        return embedLinks;
    }

    public boolean isReadHistory() {
        return readHistory;
    }

    public boolean isAttachFiles() {
        return attachFiles;
    }

    public boolean isUnknown() {
        return unknown;
    }

    private Permission(final boolean kickMembers,
                       final boolean banMembers,
                       final boolean moveMembers,
                       final boolean muteMembers,
                       final boolean deafenMembers,
                       final boolean manageNicks,
                       final boolean manageMessages,
                       final boolean addReaction,
                       final boolean mentionEveryone,
                       final boolean administrator,
                       final boolean embedLinks,
                       final boolean readHistory,
                       final boolean attachFiles,
                       final boolean unknown) {
        this.kickMembers = kickMembers;
        this.banMembers = banMembers;
        this.moveMembers = moveMembers;
        this.muteMembers = muteMembers;
        this.deafenMembers = deafenMembers;
        this.manageNicks = manageNicks;
        this.manageMessages = manageMessages;
        this.addReaction = addReaction;
        this.mentionEveryone = mentionEveryone;
        this.administrator = administrator;
        this.embedLinks = embedLinks;
        this.readHistory = readHistory;
        this.attachFiles = attachFiles;
        this.unknown = unknown;
    }

    public static class PermissionBuilder {
        private boolean kickMembers;
        private boolean banMembers;
        private boolean moveMembers;
        private boolean muteMembers;
        private boolean deafenMembers;
        private boolean manageNicks;
        private boolean manageMessages;
        private boolean addReaction;
        private boolean mentionEveryone;
        private boolean administrator;
        private boolean embedLinks;
        private boolean readHistory;
        private boolean attachFiles;
        private boolean unknown;

        public PermissionBuilder canKickMembers(final boolean canKickMembers) {
            this.kickMembers = canKickMembers;
            return this;
        }

        public PermissionBuilder canBanMembers(final boolean canBanMembers) {
            this.banMembers = canBanMembers;
            return this;
        }

        public PermissionBuilder canMoveMembers(final boolean canMoveMembers) {
            this.moveMembers = canMoveMembers;
            return this;
        }

        public PermissionBuilder canMuteMembers(final boolean canMuteMembers) {
            this.muteMembers = canMuteMembers;
            return this;
        }

        public PermissionBuilder canDeafenMembers(final boolean canDeafenMembers) {
            this.deafenMembers = canDeafenMembers;
            return this;
        }

        public PermissionBuilder canManageNicks(final boolean canManageNicks) {
            this.manageNicks = canManageNicks;
            return this;
        }

        public PermissionBuilder canManageMessages(final boolean canManageMessages) {
            this.manageMessages = canManageMessages;
            return this;
        }

        public PermissionBuilder canAddReaction(final boolean canAddReaction) {
            this.addReaction = canAddReaction;
            return this;
        }

        public PermissionBuilder canMentionEveryone(final boolean canMentionEveryone) {
            this.mentionEveryone = canMentionEveryone;
            return this;
        }

        public PermissionBuilder canAdministrator(final boolean canAdministrator) {
            this.administrator = canAdministrator;
            return this;
        }

        public PermissionBuilder canEmbedLinks(final boolean canEmbedLinks) {
            this.embedLinks = canEmbedLinks;
            return this;
        }

        public PermissionBuilder canReadHistory(final boolean canReadHistory) {
            this.readHistory = canReadHistory;
            return this;
        }

        public PermissionBuilder canAttachFiles(final boolean canAttachFiles) {
            this.attachFiles = canAttachFiles;
            return this;
        }

        public PermissionBuilder canUnknown(final boolean canUnknown) {
            this.unknown = canUnknown;
            return this;
        }

        public Permission createPermissions() {
            return new Permission(
                    kickMembers,
                    banMembers,
                    moveMembers,
                    muteMembers,
                    deafenMembers,
                    manageNicks,
                    manageMessages,
                    addReaction,
                    mentionEveryone,
                    administrator,
                    embedLinks,
                    readHistory,
                    attachFiles,
                    unknown);
        }

    }

}
