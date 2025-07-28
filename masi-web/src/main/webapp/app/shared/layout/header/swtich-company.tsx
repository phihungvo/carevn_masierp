import Button from 'app/components/button/button';
import Flex from 'app/components/flex/flex';
import Input from 'app/components/input/input';
import { useAppSelector } from 'app/config/store';
import useAccount from 'app/hooks/use-account';
import { useEffect, useState } from 'react';
import { Storage } from 'react-jhipster';
import {
  ModalBody,
  ModalFooter,
  ModalHeader,
  Modal as ModalStrap,
} from 'reactstrap';

const iconSwitch = () => (
  <svg
    width="24"
    height="24"
    viewBox="0 0 24 24"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <rect width="24" height="24" rx="12" fill="#E0ECF9" />
    <path
      d="M15.332 5.33325L17.9987 7.99992L15.332 10.6666"
      stroke="#4479C5"
      stroke-width="1.5"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
    <path
      d="M6 11.3333V10.6667C6 9.95942 6.28095 9.28115 6.78105 8.78105C7.28115 8.28095 7.95942 8 8.66667 8H18"
      stroke="#4479C5"
      stroke-width="1.5"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
    <path
      d="M8.66667 18.6666L6 15.9999L8.66667 13.3333"
      stroke="#4479C5"
      stroke-width="1.5"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
    <path
      d="M18 12.6667V13.3334C18 14.0407 17.719 14.7189 17.219 15.219C16.7189 15.7191 16.0406 16.0001 15.3333 16.0001H6"
      stroke="#4479C5"
      stroke-width="1.5"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
  </svg>
);

const iconSwitch2 = () => (
  <svg
    width="56"
    height="56"
    viewBox="0 0 56 56"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <rect x="4" y="4" width="48" height="48" rx="24" fill="#E0ECF9" />
    <rect
      x="4"
      y="4"
      width="48"
      height="48"
      rx="24"
      stroke="#F1F6FD"
      stroke-width="8"
    />
    <path
      d="M33 17L37 21M37 21L33 25M37 21H23C21.9391 21 20.9217 21.4214 20.1716 22.1716C19.4214 22.9217 19 23.9391 19 25V27M23 39L19 35M19 35L23 31M19 35H33C34.0609 35 35.0783 34.5786 35.8284 33.8284C36.5786 33.0783 37 32.0609 37 31V29"
      stroke="#4072D0"
      stroke-width="2"
      stroke-linecap="round"
      stroke-linejoin="round"
    />
  </svg>
);

const { usePatchChangeAccountCompany } = useAccount;

const AUTH_TOKEN_KEY = 'jhi-authenticationToken';

const SwitchCompany = () => {
  const [isOpen, setIsOpen] = useState<boolean>(false);
  const [selected, setSelected] = useState<string>(null);

  const account = useAppSelector(state => state?.authentication?.account);

  const { mutate } = usePatchChangeAccountCompany();

  const toggle = () => {
    setIsOpen(prev => !prev);
  };

  const confirm = () => {
    toggle();
    mutate(selected, {
      onSuccess: data => {
        const jwt = data?.data?.id_token;
        if (jwt) {
          Storage.local.set(AUTH_TOKEN_KEY, jwt);
          window.location.reload();
        }
      },
    });
  };

  useEffect(() => {
    if (account) setSelected(account?.companyId);
  }, [account]);

  return (
    <>
      <Flex
        align="center"
        className="switch-company_btn"
        gap={6}
        onClick={toggle}
      >
        <span>{account?.company?.name}</span>
        {iconSwitch()}
      </Flex>
      <ModalStrap
        isOpen={isOpen}
        toggle={toggle}
        style={{ minWidth: '480px', maxWidth: '80%' }}
        zIndex={98}
        className="switch-company_modal"
      >
        <ModalHeader className="switch-company_modal-header">
          <Flex align="center" gap={20}>
            {iconSwitch2()}{' '}
            <span style={{ fontSize: '18px', fontWeight: '600' }}>
              Đổi công ty
            </span>
          </Flex>
        </ModalHeader>
        <ModalBody className="switch-company_modal-body">
          {(account?.companies ?? [])?.map(x => (
            <Flex
              direction="column"
              gap={12}
              onClick={() => setSelected(x?.normalizedName)}
            >
              <Flex className="switch-company_modal-body-item" align="center">
                <Flex
                  className="switch-company_modal-body-item-left"
                  direction="column"
                >
                  <span>{x?.name}</span>
                  <span>{x?.normalizedName}</span>
                </Flex>
                <Input
                  type="checkbox"
                  checked={selected === x?.normalizedName}
                  className="switch-company_modal-body-item-checkbox"
                  onChange={() => setSelected(x?.normalizedName)}
                />
              </Flex>
            </Flex>
          ))}
        </ModalBody>
        <ModalFooter className="switch-company_modal-footer">
          <Flex justify="space-between" gap={12} style={{ width: '100%' }}>
            <Button
              className="switch-company_modal-footer-close"
              onClick={toggle}
            >
              Đóng
            </Button>
            <Button
              className="switch-company_modal-footer-confirm"
              color="primary"
              onClick={confirm}
            >
              Xác nhận
            </Button>
          </Flex>
        </ModalFooter>
      </ModalStrap>
    </>
  );
};

export default SwitchCompany;
