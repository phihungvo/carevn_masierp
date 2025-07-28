import { iconPath } from "app/shared/util/format";
import { ComponentProps } from "react";
import ButtonV2 from "./ButtonV2";

type Props = Omit<ComponentProps<typeof ButtonV2>, 'left_section'>

const ButtonFilter = (props: Props) => {
  return (
    <ButtonV2
      {...props}
      left_section={
        <img src={iconPath('three-line-filter.svg')} alt="filter" />
      }
    >
      Bộ lọc
    </ButtonV2>
  );
}

export default ButtonFilter
