import axios from "axios";

import { IUserGroups } from "app/shared/model/admin-users";
import { IAuthority } from "app/shared/model/authority.model";
import { adminAdminUsersEndpoints } from "app/constants/endpoints";
import { GROUP_ACTION } from "app/shared/model/enumerations/group.enum";

const GetAdminUserGroups = async (id: string) => {
    const url = adminAdminUsersEndpoints.getAdminUserGroups(id);
    return await axios.get<IAuthority[]>(url);
};

const postAdminUserGroups = async (data: IUserGroups, action: GROUP_ACTION = GROUP_ACTION.REPLACE) => {
    const url = adminAdminUsersEndpoints.postAdminUserGroups(action);
    return await axios.post<IAuthority>(url, data);
};

export default {
    GetAdminUserGroups,
    postAdminUserGroups
}
