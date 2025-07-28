import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  label: string;
  name: Path<T>;
};

const WhoCalculation = <T = any>(props: Props<T>) => {
  const { label, name } = props;

  return <WrapSelect {...props} label={label} name={name} />;
};

export default WhoCalculation;
