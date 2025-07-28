import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import contactTypeService from 'app/services/contact-type.service';
import { IContactTypeParams } from 'app/shared/model/contact-type.model';

const { CONTACT_TYPES, CONTACT_TYPE } = QUERY_KEY;
const { CREATE_CONTACT_TYPE, UPDATE_CONTACT_TYPE, DELETE_CONTACT_TYPE } = MUTATION_KEY;

const useGetContactTypes = (filter?: IContactTypeParams) => {
  return useQuery({
    queryKey: [CONTACT_TYPES, filter?.page, filter?.size],
    queryFn: () => contactTypeService.getContactTypes(filter),
    select: data => data?.data,
  });
};

const usePostContactType = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_CONTACT_TYPE],
    mutationFn: contactTypeService.createContactType,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTACT_TYPES] });
      onOk && onOk();
    },
  });
};

const usePatchContactType = (id: string, onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_CONTACT_TYPE, id],
    mutationFn: (data: any) => contactTypeService.updateContactType(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTACT_TYPES] });
      queryClient.invalidateQueries({ queryKey: [CONTACT_TYPE] });
      onOk && onOk();
    },
  });
};

const useGetContactTypeById = (id: string) => {
  return useQuery({
    queryKey: [CONTACT_TYPE, id],
    queryFn: () => contactTypeService.getContactTypeById(id),
    select: data => data?.data,
    enabled: !!id,
  });
};

const useDeleteContactType = (onOk?: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_CONTACT_TYPE],
    mutationFn: contactTypeService.deleteContactType,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [CONTACT_TYPES] });
      onOk && onOk();
    },
  });
};

export default {
  useGetContactTypes,
  usePostContactType,
  usePatchContactType,
  useGetContactTypeById,
  useDeleteContactType,
};
