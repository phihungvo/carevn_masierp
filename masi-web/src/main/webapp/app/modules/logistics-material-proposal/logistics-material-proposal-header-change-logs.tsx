import React from 'react';
import { Button } from 'reactstrap';

import { PATH } from 'app/constants/path';
import { useNavigate } from 'react-router';

function LogisticsMaterialProposalChangelogsHeader() {

    const navigation = useNavigate()

    return (
        <div className="card-header-container">
            <Button className="bg-primary" onClick={() => navigation(PATH.MATERIAL_PROPOSAL)} >
                Quay lại
            </Button>
        </div>
    );
}

export default LogisticsMaterialProposalChangelogsHeader;
