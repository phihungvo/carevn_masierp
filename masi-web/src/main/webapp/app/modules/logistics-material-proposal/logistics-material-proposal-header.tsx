import React from 'react'

import Button from 'app/components/button/button';
import InputSearch from 'app/components/input/input-search';
import ButtonIcon from 'app/components/button-icon/button-icon';

interface ILogisticsMaterialProposalHeaderProps {
    setSearchText: (searchText: string) => void;
    toggleFilter: () => void;
    toggleCreate: () => void;
}

export default function LogisticsMaterialProposalHeader({ setSearchText, toggleFilter, toggleCreate }: ILogisticsMaterialProposalHeaderProps) {
    return (
        <div className="card-header-container">
            <div className="card-header-extra">
                <InputSearch
                    className="card-header-extra"
                    onChange={e => {
                        setSearchText(e.target.value);
                    }}
                />
            </div>
            <div className="card-header-extra" >
                <Button className="btn-filter" onClick={toggleFilter} >
                    Lọc <img src="content/images/vuesax/linear/sort.svg" alt="filter" />
                </Button>
                <Button color="primary" onClick={toggleCreate}>
                    Tạo mới
                </Button>
                <ButtonIcon
                    icon={<img className="document-download" src="content/images/vuesax/linear/document-download.svg" alt="download" />}
                />
            </div>
        </div>
    )
}
