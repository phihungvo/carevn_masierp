import { useState } from 'react';
import { useNavigate } from 'react-router';

export const useModalsAsset = () => {
    const [openFilter, setOpenFilter] = useState(false);
    const toggleFilter = () => setOpenFilter(!openFilter);

    return [
        { openFilter, toggleFilter },
    ];
};
