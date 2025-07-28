import { PATH } from 'app/constants/path';
import { useState } from 'react';
import { useNavigate } from 'react-router';

export const useModalsAllocation = () => {
    const navigate = useNavigate()
    const [openCreate, setOpenCreate] = useState(false);
    const [openFilter, setOpenFilter] = useState(false);
    const [openUpdate, setOpenUpdate] = useState(false);
    const [openDetail, setOpenDetail] = useState(false);
    const [openCreateSuccess, setOpenCreateSuccess] = useState(false);

    const toggleCreateSuccess = () => setOpenCreateSuccess(!openCreateSuccess);
    const toggleDetail = () => setOpenDetail(!openDetail);
    const toggleUpdate = () => setOpenUpdate(!openUpdate);
    const toggleFilter = () => setOpenFilter(!openFilter);
    const toggleCreate = () => setOpenCreate(!openCreate);
    const toggleNavigateCreate = () => navigate(PATH.ALLOCATION_CREATE);

    return [
        {toggleNavigateCreate},
        { openCreate, toggleCreate },
        { openFilter, toggleFilter },
        { openUpdate, toggleUpdate },
        { openDetail, toggleDetail },
        { openCreateSuccess, toggleCreateSuccess },
    ];
};
