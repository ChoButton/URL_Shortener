import React from 'react';
import Modal from 'react-bootstrap/Modal';
import Button from 'react-bootstrap/Button';

// 각종 에러에 대한 모달창
export const ErrorMessageModal = ({ show, onHide, message }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>오류</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>{message}</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>닫기</Button>
            </Modal.Footer>
        </Modal>
    );
};

//삭제에 대한 모달창
export const ConfirmDeleteModal = ({ show, onHide, content, onConfirm }) => {
    return (
        <Modal show={show} onHide={onHide} centered>
            <Modal.Header closeButton>
                <Modal.Title>삭제 확인</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <p>{content}</p>
                <p>정말 삭제하시겠습니까?</p>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={onHide}>취소</Button>
                <Button variant="danger" onClick={onConfirm}>삭제</Button>
            </Modal.Footer>
        </Modal>
    );
};
