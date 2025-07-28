import { PATH } from 'app/constants/path';
import { useState } from 'react';
import { useNavigate } from 'react-router';

export const useModalsDepreciation = () => {
    const navigate = useNavigate()
    const [openCreate, setOpenCreate] = useState(false);
    const [openFilter, setOpenFilter] = useState(false);
    const [openUpdate, setOpenUpdate] = useState(false);
    const [openDetail, setOpenDetail] = useState(false);

    const toggleDetail = () => setOpenDetail(!openDetail);
    const toggleUpdate = () => setOpenUpdate(!openUpdate);
    const toggleFilter = () => setOpenFilter(!openFilter);
    const toggleCreate = () => navigate(PATH.DEPRECIATION_CREATE);

    return [
        { openCreate, toggleCreate },
        { openFilter, toggleFilter },
        { openUpdate, toggleUpdate },
        { openDetail, toggleDetail },
    ];
};
