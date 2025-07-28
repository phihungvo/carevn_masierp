import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";

import adminUsersService from "app/services/admin-users.service";
import { IUserGroups } from "app/shared/model/admin-users";
import { MUTATION_KEY, QUERY_KEY } from "app/constants/query-key";
import { GROUP_ACTION } from "app/shared/model/enumerations/group.enum";

const { CREATE_ADMIN_USER_GROUP } = MUTATION_KEY;
const { ADMIN_USER_GROUP, EMPLOYEE_PROFILES } = QUERY_KEY;

const useGetAdminUserGroups = (id: string) => {
    return useQuery({
        queryKey: [ADMIN_USER_GROUP],
        queryFn: () => adminUsersService.GetAdminUserGroups(id),
        select: data => data.data,
        enabled: !!id
    });
};

const usePostAdminUserGroups = (toggle?: () => void, action?: GROUP_ACTION) => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationKey: [CREATE_ADMIN_USER_GROUP],
        mutationFn: (data: IUserGroups) => adminUsersService.postAdminUserGroups(data, action),
        onSuccess() {
            toggle && toggle()
            queryClient.invalidateQueries({ queryKey: [ADMIN_USER_GROUP] });
            queryClient.invalidateQueries({ queryKey: [EMPLOYEE_PROFILES] });
        },
    });
};

export default {
    useGetAdminUserGroups,
    usePostAdminUserGroups
}