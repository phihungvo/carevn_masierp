import { dayjsRangeSelect } from "app/shared/util/date-utils";
import dayjs from "dayjs";
import { ComponentProps } from "react";
import { Path } from "react-hook-form";
import WrapSelect from "./WrapSelect";

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const MonthYearSelect = <T = any,>(props: Props<T>) => {
  const { name, label } = props;

  let today = dayjs().startOf('year');
  let future = dayjs().add(12, 'months');

  return (
    <WrapSelect
      {...props}
      label={label}
      name={name}
      data={dayjsRangeSelect(today, future, 'months')}
    />
  );
};

export default MonthYearSelect;
