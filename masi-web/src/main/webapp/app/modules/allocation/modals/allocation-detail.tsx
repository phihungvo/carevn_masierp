import Descriptions from '@uiw/react-descriptions';
import Flex from 'app/components/flex/flex';
import Modal from 'app/components/modal/modal';
import { Typography } from 'app/components/typography/typography';
import React from 'react'

interface AllocationDetail {
    isOpen: boolean;
    toggle: () => void;
}

const AllocationDetail = (props: AllocationDetail) => {

    const { isOpen, toggle } = props;

    return (
        <Modal
            isOpen={isOpen}
            toggle={toggle}
            titleHeader='Chi tiết khiếu nại'
            ok={false}
            cancel={false}
            className='allocation-detail'
        >
            <Flex direction="column" gap={16}>
                <Descriptions title="Thông tin chung" size="large" bordered column={2}>
                    <Descriptions.Item label="Ngày ghi nhận">
                        <Typography level="text" className="fw-bolder">
                            24/10/2002
                        </Typography>
                    </Descriptions.Item>

                    <Descriptions.Item label="Loại">
                        <Typography level="text" className="fw-bolder">
                            Nội bộ
                        </Typography>
                    </Descriptions.Item>

                    <Descriptions.Item label="Nhân viên ghi nhận">
                        <Typography level="text" className="fw-bolder">
                            Hà Hoàng Quân
                        </Typography>
                    </Descriptions.Item>

                    <Descriptions.Item label="Nhân viên phản hồi">
                        <Typography level="text" className="fw-bolder">
                            Huỳnh Phương Yến Nhy
                        </Typography>
                    </Descriptions.Item>

                    <Descriptions.Item label="Khách hàng phản hồi">
                        <Typography level="text" className="fw-bolder">
                            Nguyễn Văn A
                        </Typography>
                    </Descriptions.Item>

                    <Descriptions.Item label="Mô tả">
                        <Typography level="text" className="fw-bolder">
                            Scheduled a callback for next week.
                        </Typography>
                    </Descriptions.Item>

                    <Descriptions.Item label="Ghi chú">
                        <Typography level="text" className="fw-bolder">
                            Consider adding an FAQ section for common issues.
                        </Typography>
                    </Descriptions.Item>
                </Descriptions>

                {/* <div className="divider" /> */}
            </Flex>
        </Modal>
    )
}

export default AllocationDetail
