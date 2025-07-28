import React, { ComponentProps } from 'react';
import ButtonV2 from './ButtonV2';

type Props = Omit<ComponentProps<typeof ButtonV2>, 'children'> & {
  text: string | React.ReactNode;
};

const ButtonAdd = (props: Props) => {
  const { text, variant = 'fill', ...rest } = props;

  return (
    <ButtonV2
      variant={variant}
      color="blue"
      {...rest}
      type="button"
      left_section={
        variant === 'primary' ? (
          <img src="content/images/vuesax/linear/add_white.svg" alt="plus" />
        ) : (
          <img src="content/images/vuesax/linear/add_blue.svg" alt="plus" />
        )
      }
    >
      {text}
    </ButtonV2>
  );
};

export default ButtonAdd;
