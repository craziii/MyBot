public class Permission{

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

    Permission(final boolean kickMembers, final boolean banMembers, final boolean moveMembers, final boolean muteMembers, final boolean deafenMembers, final boolean manageNicks, final boolean manageMessages, final boolean addReaction, final boolean mentionEveryone, final boolean administrator, final boolean embedLinks, final boolean readHistory, final boolean attachFiles, final boolean unknown){
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

    public static class PermissionBuilder{
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

    public PermissionBuilder(){

    }

    public PermissionBuilder canKickMembers(boolean canKickMembers){
        this.kickMembers = canKickMembers;
        return this;
    }

    public PermissionBuilder canBanMembers(boolean canBanMembers){
        this.banMembers = canBanMembers;
        return this;
    }

    public PermissionBuilder canMoveMembers(boolean canMoveMembers){
        this.moveMembers = canMoveMembers;
        return this;
    }

    public PermissionBuilder canMuteMembers(boolean canMuteMembers){
        this.muteMembers = canMuteMembers;
        return this;
    }

    public PermissionBuilder canDeafenMembers(boolean canDeafenMembers){
        this.deafenMembers = canDeafenMembers;
        return this;
    }

    public PermissionBuilder canManageNicks(boolean canManageNicks){
        this.manageNicks = canManageNicks;
        return this;
    }

    public PermissionBuilder canManageMessages(boolean canManageMessages){
        this.manageMessages = canManageMessages;
        return this;
    }

    public PermissionBuilder canAddReaction(boolean canAddReaction){
        this.addReaction = canAddReaction;
        return this;
    }

    public PermissionBuilder canMentionEveryone(boolean canMentionEveryone){
        this.mentionEveryone = canMentionEveryone;
        return this;
    }

    public PermissionBuilder canAdministrator(boolean canAdministrator){
        this.administrator = canAdministrator;
        return this;
    }

    public PermissionBuilder canEmbedLinks(boolean canEmbedLinks){
        this.embedLinks = canEmbedLinks;
        return this;
    }

    public PermissionBuilder canReadHistory(boolean canReadHistory){
        this.readHistory = canReadHistory;
        return this;
    }

    public PermissionBuilder canAttachFiles(boolean canAttachFiles){
        this.attachFiles = canAttachFiles;
        return this;
    }

    public PermissionBuilder canUnknown(boolean canUnknown){
        this.unknown = canUnknown;
        return this;
    }

    public Permission createPermissions(){
        return new Permission(kickMembers, banMembers, moveMembers, muteMembers, deafenMembers, manageNicks, manageMessages, addReaction, mentionEveryone, administrator, embedLinks, readHistory, attachFiles, unknown)
    }

}
