import { ComponentProps } from 'react';
import { Path } from 'react-hook-form';
import WrapSelect from './WrapSelect';
import { statusSelectData } from 'app/modules/asset/constants/status';

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const Status = <T = any>(props: Props<T>) => {
  const { name, label } = props;

  return (
    <WrapSelect { ...props } label={label} name={name} data={statusSelectData} />
  )
};

export default Status;
