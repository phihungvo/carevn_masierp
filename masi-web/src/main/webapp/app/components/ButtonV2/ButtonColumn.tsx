import { ComponentProps } from "react";
import ButtonV2 from "./ButtonV2";
import { iconPath } from "app/shared/util/format";

type Props = Omit<ComponentProps<typeof ButtonV2>, 'left_section'>

const ButtonColumn = (props: Props) => {
  return (
    <ButtonV2
      {...props}
      left_section={<img src={iconPath('settings.svg')} alt="filter" />}
    >
      Cột
    </ButtonV2>
  );
}

export default ButtonColumn
