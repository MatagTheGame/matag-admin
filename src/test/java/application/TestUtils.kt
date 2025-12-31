package application

import com.matag.admin.user.MatagUser
import com.matag.admin.user.MatagUserStatus
import com.matag.admin.user.MatagUserType

object TestUtils {
    const val GUEST_USERNAME: String = "Guest"
    const val GUEST_SESSION_TOKEN_1: String = "00000000-0000-0000-0001-000000000001"
    const val GUEST_SESSION_TOKEN_2: String = "00000000-0000-0000-0001-000000000002"

    const val USER_1_SESSION_TOKEN: String = "00000000-0000-0000-0000-000000000001"
    const val USER_1_USERNAME: String = "User1"

    const val USER_2_SESSION_TOKEN: String = "00000000-0000-0000-0000-000000000002"
    const val USER_2_USERNAME: String = "User2"

    const val INACTIVE_USER_USERNAME: String = "InactiveUser"

    var PASSWORD: String = "password"
    var PASSWORD_ENCODED: String =
        "{argon2}\$argon2id\$v=19\$m=65536,t=4,p=8\$kfWxCBLq0XIjaaG8LxfrQg\$FkuvunHdrO2m+Dw85b33OUSY7uONpyVCgppJg+BYjsM"

    fun guest(): MatagUser {
        return MatagUser.builder()
            .username(GUEST_USERNAME)
            .password(PASSWORD_ENCODED)
            .emailAddress("guest@matag.com")
            .status(MatagUserStatus.ACTIVE)
            .type(MatagUserType.GUEST)
            .build()
    }

    fun user1(): MatagUser {
        return MatagUser.builder()
            .username(USER_1_USERNAME)
            .password(PASSWORD_ENCODED)
            .emailAddress("user1@matag.com")
            .status(MatagUserStatus.ACTIVE)
            .type(MatagUserType.USER)
            .build()
    }

    fun user2(): MatagUser {
        return MatagUser.builder()
            .username(USER_2_USERNAME)
            .password(PASSWORD_ENCODED)
            .emailAddress("user2@matag.com")
            .status(MatagUserStatus.ACTIVE)
            .type(MatagUserType.USER)
            .build()
    }

    fun inactive(): MatagUser {
        return MatagUser.builder()
            .username(INACTIVE_USER_USERNAME)
            .password(PASSWORD_ENCODED)
            .emailAddress("inactiveUser@matag.com")
            .status(MatagUserStatus.INACTIVE)
            .type(MatagUserType.USER)
            .build()
    }
}
