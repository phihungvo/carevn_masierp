import { useState } from 'react';

export const useModalsAuthorities = () => {
  const [openUpdateUsers, setOpenUpdateUsers] = useState(false);
  const [openDetail, setOpenDetail] = useState(false);
  const [openUpdateSuccess, setOpenUpdateSuccess] = useState(false);
  const [isOpenUpdateModal, setIsOpenUpdateModal] = useState<boolean>(false)
  const [isOpenSettingCompanies, setIsOpenSettingCompanies] = useState(false)

  const toggleUpdateSuccess = () => setOpenUpdateSuccess(!openUpdateSuccess)
  const toggleDetail = () => setOpenDetail(!openDetail)
  const toggleUpdateUsers = () => setOpenUpdateUsers(!openUpdateUsers)
  const toggleUpdateAccount = () => setIsOpenUpdateModal(!isOpenUpdateModal)
  const toggleSettingCompanies = () => setIsOpenSettingCompanies(!isOpenSettingCompanies)

  return [
    { openUpdateUsers, toggleUpdateUsers },
    { openDetail, toggleDetail },
    { openUpdateSuccess, toggleUpdateSuccess },
    { isOpenUpdateModal, toggleUpdateAccount },
    { isOpenSettingCompanies, toggleSettingCompanies }
  ];
};
