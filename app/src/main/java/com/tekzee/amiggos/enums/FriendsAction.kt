package com.tekzee.amiggos.enums

enum class FriendsAction(val action: String) {
    ACCEPT("ACCEPT"),
    REJECT("REJECT"),
    CLICK("CLICK"),
    SHOW_FRIEND_REQUEST("SHOW_FRIEND_REQUEST"),
    SHOW_FRIENDS("SHOW_FRIENDS"),
    PARTY_INVITATIONS("PARTY_INVITATIONS"),
    ACCEPT_PARTY_INVITATIONS("ACCEPT_PARTY_INVITATIONS"),
    ACCEPT_CREATE_MEMORY_INVITATIONS("ACCEPT_CREATE_MEMORY_INVITATIONS"),
    CREATE_MEMORY_INVITATIONS("CREATE_MEMORY_INVITATIONS"),
    REJECT_PARTY_INVITATIONS("REJECT_PARTY_INVITATIONS"),
    REJECT_CREATE_MEMORY_INVITATIONS("REJECT_CREATE_MEMORY_INVITATIONS"),
    SHOW_MY_MEMORY("SHOW_MY_MEMORY");

}