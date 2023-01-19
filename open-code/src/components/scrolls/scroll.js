import './scroll.css'

export const Scroll = (props)=>{
    console.log(props.img)
    return(
        <>
            <div className={"scroll"}>
                <div>
                    <h1>
                        {props.heading}
                    </h1>
                    <div style={{width : '50vw'}}>
                        <p>
                            {props.detail}
                        </p>
                    </div>

                </div>
                <img src={props.img} style={{width : props.width+'%',height :props.height+'%'}}/>
            </div>
        </>
    )
}
