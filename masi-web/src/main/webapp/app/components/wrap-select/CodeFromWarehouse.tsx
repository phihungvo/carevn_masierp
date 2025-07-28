import { ComponentProps } from "react";
import WrapSelect from "./WrapSelect";
import { Path } from "react-hook-form";

type Props<T> = Partial<Omit<ComponentProps<typeof WrapSelect>, 'name'>> & {
  name: Path<T>;
  label?: string;
};

const CodeFromWarehouse = <T = any>(props: Props<T>) => {
  return <WrapSelect {...props} label="Mã từ kho" name="a" />;
};

export default CodeFromWarehouse;
