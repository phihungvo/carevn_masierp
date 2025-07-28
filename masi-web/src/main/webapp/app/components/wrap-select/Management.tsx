import { ComponentProps } from "react";
import WrapSelect from "./WrapSelect";
import { Path } from "react-hook-form";

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const Management = <T = any>(props: Props<T>) => {
  return <WrapSelect {...props} label="Mã nhóm" name="a" />;
};

export default Management;
