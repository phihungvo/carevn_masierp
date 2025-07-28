import configService from 'app/services/config.service'
import { useQuery } from "@tanstack/react-query";
import { QUERY_KEY } from "app/constants/query-key";
import { IParameterConfig } from "app/shared/model/config.model";

const { CONFIG } = QUERY_KEY;

const useGetConfig = (key?: string, data?: IParameterConfig) => {
    return useQuery({
        queryKey: [CONFIG],
        queryFn: () => configService.getConfig(key || 'MAX_WORKING_HOURS', data || { value: 8, type: 'NUMBER', description: '' }),
        select: data => data?.data,
    });
};

export default {
    useGetConfig
}