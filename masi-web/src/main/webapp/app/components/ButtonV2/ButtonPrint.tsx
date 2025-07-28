import { ComponentProps } from 'react';
import ButtonV2 from './ButtonV2';

type Props = Omit<ComponentProps<typeof ButtonV2>, 'children'> & {
  children?: React.ReactNode;
};

const ButtonPrint = (props: Props) => {
  return (
    <ButtonV2
      {...props}
      left_section={
        <img src="content/images/vuesax/linear/printer.svg" alt="plus" />
      }
      onClick={e => {
        if (props?.onClick) props.onClick(e);
        else setTimeout(() => window.print(), 500);
      }}
    >
      {props?.children}
    </ButtonV2>
  );
};

export default ButtonPrint;
