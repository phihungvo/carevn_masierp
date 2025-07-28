import { QueryObserverOptions, useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import useModalRedux from "app/hooks/use-modal-redux";
import dayjs from "dayjs";
import { useState } from "react";
import { useParams } from "react-router";
import { incomingInvoiceApi } from "./api.axios";

const INCOMING_INVOICE_LIST = 'INCOMING_INVOICE_V2_LIST';
const INCOMING_INVOICE_DETAIL = 'INCOMING_INVOICE_V2_DETAIL';
const INCOMING_INVOICE_V2_NEXT_CODE = 'INCOMING_INVOICE_V2_NEXT_CODE';
const PATMENT_REQUEST_EXPORT_EXCEL = 'PATMENT_REQUEST_EXPORT_EXCEL';

export const useIncomingInvoiceDetail = (
  id?: string,
) => {
  const incommingQuery = useQuery({
    queryKey: [INCOMING_INVOICE_DETAIL, id],
    queryFn: incomingInvoiceApi.detail(id),
    enabled: !!id,
  });

  return incommingQuery;
}

export const useIncomingInvoiceList = (
  filter: any,
  options?: Partial<QueryObserverOptions>
) => {
  return useQuery({
    queryKey: [INCOMING_INVOICE_LIST, filter],
    queryFn: incomingInvoiceApi.list(filter),
    ...options,
  });
}

export const useIncomingInvoiceCreate = (isImportInvoice: boolean) => {
  const queryClient = useQueryClient();
  const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux();

  return useMutation({
    mutationFn: incomingInvoiceApi.create,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [INCOMING_INVOICE_LIST],
      });
      handleToggleSuccessModal({ content: `Tạo hóa đơn ${isImportInvoice ? 'nhập khẩu' : 'đầu vào'} thành công` });
    },
    onError: () => {
      handleToggleFailModal({ content: `Tạo hóa đơn ${isImportInvoice ? 'nhập khẩu' : 'đầu vào'} thất bại` });
    },
  });
}

export const useIncomingInvoiceUpdate = (
  id: string,
  isImportInvoice: boolean
) => {
  const queryClient = useQueryClient();
  const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux();

  return useMutation({
    mutationFn: incomingInvoiceApi.update(id),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [INCOMING_INVOICE_LIST],
      });
      queryClient.invalidateQueries({
        queryKey: [INCOMING_INVOICE_DETAIL, id],
      });
      handleToggleSuccessModal({ content: `Cập nhật hóa đơn ${isImportInvoice ? 'nhập khẩu' : 'đầu vào'} thành công` });
    },
    onError: () => {
      handleToggleFailModal({ content: `Cập nhật hóa đơn ${isImportInvoice ? 'nhập khẩu' : 'đầu vào'} thất bại` });
    },
  });
}

export const useIncomingInvoiceNextCode = (enabled: boolean) => {
  let format = dayjs().format('MMYY') + '/';
  const params = useParams()
  const isImportForm = params?.type === 'import-form';
  let header = isImportForm ? 'HDNK' : 'HDDV';
  return useQuery({
    queryKey: [INCOMING_INVOICE_V2_NEXT_CODE],
    queryFn: incomingInvoiceApi.nextCode,
    select: (res) => {
      return header + format + res?.data?.currentSequence.toString().padStart(4, '0');
    },
    enabled
  });
}

export const useIncomingInvoiceCancel = (payload: { id?: string, filter?: any}) => {
  let { filter, id } = payload;
  const queryClient = useQueryClient();
  const { handleToggleSuccessModal, handleToggleFailModal } = useModalRedux();

  return useMutation({
    mutationFn: incomingInvoiceApi.cancel,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: [INCOMING_INVOICE_LIST],
      });
      queryClient.invalidateQueries({
        queryKey: [INCOMING_INVOICE_DETAIL, id],
      });
      handleToggleSuccessModal({ content: 'Hủy hóa đơn thành công' }, false);
    },
    onError: () => {
      handleToggleFailModal({ content: 'Hủy hóa đơn thất bại' }, false);
    },
  });
}

export const useExportIncommingInvoice = () => {
  const queryClient = useQueryClient();
  const [enabled, setEnabled] = useState(false);

  const query = useQuery({
    queryKey: [PATMENT_REQUEST_EXPORT_EXCEL],
    queryFn: incomingInvoiceApi.exportExcel,
    enabled,
  });

  const trigger = () => {
    setEnabled(true);
  };

  if (query.isSuccess) {
    queryClient.resetQueries({ queryKey: [PATMENT_REQUEST_EXPORT_EXCEL] });
  }

  if ((query.isSuccess || query.isError) && enabled) {
    setEnabled(false);
  }

  return { trigger, ...query };
}
