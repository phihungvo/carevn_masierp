import { useEffect } from 'react';
import { useFormContext } from 'react-hook-form';
import { useSearchParams } from 'react-router-dom';
import { DeliveryScheduleSchema } from './validations/delivery-schduler.validate';
import { reset } from 'app/entities/region/region.reducer';
import { useAppSelector } from 'app/config/store';

export const table = 'table';
export const calendar = 'calendar';
export const purchase = 'purchase';
export const sell = 'sell';
export const type = 'type';
export const mode = 'mode';
export const purchasePath = `?${mode}=${purchase}`;
export const sellPath = `?${mode}=${sell}`;
export const formType = 'formType';

export type DeliverySearchParams = {
  type: 'table' | 'calendar';
  mode: 'purchase' | 'sell';
  formType: 'many' | 'create' | 'update';
  isOpenModal: boolean;
  date: string;
};

export type TogglePayload = Partial<{
  formType: 'create' | 'update' | 'many';
  date: string;
  orderCode: string;
  createdBy: string;
}>;

const useDeliverySearchParams = <T = any>() => {
  const authorities = useAppSelector(
    state => state.authentication.account.authorities,
  );

  const [searchParams, setSearchParams] = useSearchParams();
  const isPurchase = searchParams.get(mode) === purchase;
  const isSell = searchParams.get(mode) === sell;
  const isTable = searchParams.get(type) === table;
  const isCalendar = searchParams.get(type) === calendar;
  const isEmptyType = !searchParams.get(type);
  const isEdit = searchParams.get(formType) === 'update';
  const isCreate = searchParams.get(formType) === 'create';
  const isOpenModal = searchParams.get('isOpenModal') === 'true';
  const isMany = searchParams.get(formType) === 'many';

  const onToggleModal = (payload?: TogglePayload) => () => {
    setSearchParams(pre => {
      let preOpen = pre.get('isOpenModal');
      pre.set('isOpenModal', preOpen === 'true' ? 'false' : 'true');
      payload?.formType && pre.set('formType', payload.formType);
      payload?.formType && pre.set('date', payload.date);
      if (pre.get('isOpenModal') === 'false') {
        pre.delete('formType');
        pre.delete('isOpenModal');
        pre.delete('date');
      }
      return pre;
    });
  };

  const onSwitchScreen = () => {
    setSearchParams(pre => {
      let isManyScreen = pre.get('formType') === 'many';
      pre.set('formType', isManyScreen ? 'update' : 'many');
      return pre;
    });
  };

  useEffect(() => {
    isEmptyType && setSearchParams({ [type]: table, [mode]: purchase });
  }, []);

  return {
    isPurchase,
    isSell,
    isTable,
    isCalendar,
    searchParams: Object.fromEntries(
      searchParams as any,
    ) as DeliverySearchParams,
    isEdit,
    isOpenModal: isOpenModal ? true : false,
    onToggleModal,
    isCreate,
    isMany,
    onSwitchScreen
  };
};

export default useDeliverySearchParams;
