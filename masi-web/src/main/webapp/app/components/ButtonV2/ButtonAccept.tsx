import { ComponentProps } from "react";
import ButtonV2 from "./ButtonV2";

type Props = Omit<ComponentProps<typeof ButtonV2>, 'variant' | 'left_section' | 'right_section'>

const ButtonAccept = (props: Props) => {
  return (
    <ButtonV2
      {...props}
      variant="text"
      left_section={
        <img src="content/images/vuesax/linear/check_green.svg" alt="cancel" />
      }
    >
      Duyệt
    </ButtonV2>
  );
}

export default ButtonAccept
