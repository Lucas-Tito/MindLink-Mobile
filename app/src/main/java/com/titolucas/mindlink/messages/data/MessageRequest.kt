package com.titolucas.mindlink.messages.data

import android.os.Parcel
import android.os.Parcelable

data class MessageRequest(
    val senderId: String,
    val receiverId: String,
    val text: String,
    val participants: List<String>,
    val messageId: String,
    val createdAt: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(senderId)
        parcel.writeString(receiverId)
        parcel.writeString(text)
        parcel.writeStringList(participants)
        parcel.writeString(messageId)
        parcel.writeString(createdAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MessageRequest> {
        override fun createFromParcel(parcel: Parcel): MessageRequest {
            return MessageRequest(parcel)
        }

        override fun newArray(size: Int): Array<MessageRequest?> {
            return arrayOfNulls(size)
        }
    }
}