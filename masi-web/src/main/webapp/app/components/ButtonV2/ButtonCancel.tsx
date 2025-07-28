import { ComponentProps } from "react";
import ButtonV2 from "./ButtonV2";

type Props = Omit<ComponentProps<typeof ButtonV2>, 'variant' | 'left_section' | 'right_section'>

const ButtonCancel = (props: Props) => {
  return (
    <ButtonV2
      {...props}
      variant="text"
      left_section={
        <img src="content/images/vuesax/linear/x-circle.svg" alt="cancel" />
      }
    >
      Hủy
    </ButtonV2>
  );
}

export default ButtonCancel
