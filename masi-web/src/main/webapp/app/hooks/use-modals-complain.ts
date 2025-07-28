import { useState } from 'react';
import { useNavigate } from 'react-router';

export const useModalsComplain = () => {
  const navigate = useNavigate();
  const [openCreate, setOpenCreate] = useState(false);
  const [openFilter, setOpenFilter] = useState(false);
  const [openCreateSuccess, setOpenCreateSuccess] = useState(false);

  const toggleCreateSuccess = () => setOpenCreateSuccess(!openCreateSuccess);
  const toggleFilter = () => setOpenFilter(!openFilter);
  const toggleCreate = () => setOpenCreate(!openCreate);
  const toggleNavigateCreate = () =>
    navigate('/customer-services/complain/create');

  return [
    { toggleNavigateCreate },
    { openCreate, toggleCreate },
    { openFilter, toggleFilter },
    { openCreateSuccess, toggleCreateSuccess },
  ];
};
