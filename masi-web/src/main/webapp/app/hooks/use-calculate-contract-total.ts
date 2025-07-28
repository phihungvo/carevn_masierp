import { ContractFormSchema } from 'app/validation/contract.validation';
import { useEffect } from 'react';
import { Path } from 'react-hook-form';

export const useCalculateContractTotal = <T extends object = {}>(
  exchangeRate: string,
  additives: ContractFormSchema['additives'],
  name: Path<T>,
  setValue: (name: Path<T>, value: string) => void,
) => {
  useEffect(() => {
    const totalPrice =
      additives?.reduce((acc, additive) => {
        acc += Number(additive?.price) * Number(additive?.quantity);

        return acc;
      }, 0) * Number(exchangeRate);

    setValue(name, parseFloat(totalPrice.toFixed(1)).toString());
  }, [exchangeRate, additives, additives?.[additives?.length - 1]?.price, additives?.[additives?.length - 1]?.quantity]);
};

export const recalculateContractTotal = <T extends object = {}>(
  exchangeRate: string,
  additives: ContractFormSchema['additives'],
  name: Path<T>,
  setValue: (name: Path<T>, value: string) => void,
) => {
  const totalPrice =
    additives?.reduce((acc, additive) => {
      acc += Number(additive?.price) * Number(additive?.quantity);

      return acc;
    }, 0) * Number(exchangeRate);

  setValue(name, parseFloat(totalPrice.toFixed(1)).toString().replace(/,/g, ''));
};
