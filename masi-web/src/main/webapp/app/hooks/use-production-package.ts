import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { MUTATION_KEY, QUERY_KEY } from 'app/constants/query-key';
import { productionPackageService } from 'app/services/production-packages.service';
import { IApiError } from 'app/shared/model/error.model';
import {
  IProductionPackage,
  IProductionPackageParams,
} from 'app/shared/model/production-package.model';
import { AxiosError } from 'axios';

const { PRODUCTION_PACKAGES } = QUERY_KEY;
const {
  CREATE_PRODUCTION_PACKAGE,
  UPDATE_PRODUCTION_PACKAGE,
  DELETE_PRODUCTION_PACKAGE,
} = MUTATION_KEY;

const useProductionPackages = (filter?: IProductionPackageParams) => {
  return useQuery({
    queryKey: [
      PRODUCTION_PACKAGES,
      filter?.page,
      filter?.size,
      filter?.search,
      filter?.manufactureOrderIds,
      filter?.moIds,
      filter?.statuses,
      filter?.isHasQC,
      filter?.isProductMaintainSpecified,
    ],
    queryFn: () => productionPackageService.getProductionPackages(filter),
    select: data => data.data,
  });
};

const useProductionPackageById = (id: string) => {
  return useQuery({
    queryKey: [PRODUCTION_PACKAGES, id],
    queryFn: () => productionPackageService.getProductionPackageById(id),
    enabled: !!id,
  });
};

const usePostProductionPackage = (
  toggle: () => void,
  toggleSuccess: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [CREATE_PRODUCTION_PACKAGE],
    mutationFn: productionPackageService.postProductionPackage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PACKAGES] });
      toggle();
      toggleSuccess();
    },
    onError: (error: AxiosError<IApiError>) => error,
  });
};

const useUpdateProductionPackage = (
  id: string,
  toggle: () => void,
  toggleSuccess: () => void,
) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [UPDATE_PRODUCTION_PACKAGE, id],
    mutationFn: (data: IProductionPackage) =>
      productionPackageService.patchProductionPackage(data, id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PACKAGES] });
      toggle();
      toggleSuccess();
    },
  });
};

const useDeleteProductionPackage = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationKey: [DELETE_PRODUCTION_PACKAGE],
    mutationFn: productionPackageService.deleteProductionPackage,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: [PRODUCTION_PACKAGES] });
    },
  });
};

export default {
  useProductionPackages,
  useProductionPackageById,
  usePostProductionPackage,
  useUpdateProductionPackage,
  useDeleteProductionPackage,
};
