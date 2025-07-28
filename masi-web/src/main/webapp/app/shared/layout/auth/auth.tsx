import './auth.scss';
import Flex from 'app/components/flex/flex';
import { Typography } from 'app/components/typography/typography';
import React from 'react';

interface IAuthContainer {
  label: string;
  children: React.ReactNode;
}

const AuthContainer = (props: IAuthContainer) => {
  const { label, children } = props;

  return (
    <Flex direction="column" align="center" className="auth">
      <img src="content/images/masilogo.png" alt="Logo" />
      <Typography level={3}>{label}</Typography>

      {children}
    </Flex>
  );
};

export default AuthContainer;
