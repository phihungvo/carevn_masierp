import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { useDownloadXlsx } from "./use-download";

const PATMENT_REQUEST_EXPORT_EXCEL = 'PATMENT_REQUEST_EXPORT_EXCEL'

export const useExportExcel = (payload: {
  fileName: string,
  axiosFn: () => any
  fileType?: string,
  key?: string,
}) => {
    let { key, axiosFn, fileName, fileType } = payload;
    const queryClient = useQueryClient();
    const [enabled, setEnabled] = useState(false);
  
    key = key || PATMENT_REQUEST_EXPORT_EXCEL

    fileName = fileName || 'excel_file'
    fileType = fileType || 'xlsx'

    const query = useQuery({
      queryKey: [key],
      queryFn: axiosFn,
      enabled,
    });
  
    const trigger = () => {
      setEnabled(true);
    };
  
    if (query.isSuccess) {
      queryClient.resetQueries({ queryKey: [key] });
    }
  
    if ((query.isSuccess || query.isError) && enabled) {
      setEnabled(false);
    }

    useDownloadXlsx(query?.data?.data, fileName, fileType);
  
    return trigger;
  }